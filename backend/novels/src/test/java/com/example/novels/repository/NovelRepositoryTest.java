package com.example.novels.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.example.novels.entity.Genre;
import com.example.novels.entity.Grade;
import com.example.novels.entity.Member;
import com.example.novels.entity.Novel;
import com.example.novels.entity.constant.MemberRole;

@SpringBootTest
public class NovelRepositoryTest {

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void userInsert() {
        IntStream.rangeClosed(11, 20).forEach(i -> {
            Member user = Member.builder()
                    .email("user" + i + "@gmail.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("user" + i)
                    .build();
            user.addRole(MemberRole.USER);

            if (i > 17) {
                user.addRole(MemberRole.MANAGER);
            }

            if (i > 19) {
                user.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(user);
        });
    }

    @Test
    public void gradeInsert() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            // novel 번호만큼
            // novel 번호가 건너뛰어서 생성되서 1~18 / 25~42 / 56 ~ 91
            // long no = (long) (Math.random() * 18) + 1;
            // long no = (long) (Math.random() * 18) + 25;
            long no = (long) (Math.random() * 18) + 56;

            Novel novel = Novel.builder().id(no).build();

            int rating = (int) (Math.random() * 5) + 1;

            int j = (int) (Math.random() * 10) + 1;

            Grade grade = Grade.builder().novel(novel).rating(rating)
                    .user(Member.builder().email("user" + j + "@gmail.com").build())
                    .build();
            gradeRepository.save(grade);
        });
    }

    @Test
    public void getTest() {
        Object[] row = novelRepository.getNovelById(14L);

        Novel n = (Novel) row[0];
        Genre ge = (Genre) row[1];
        Double avg = (Double) row[2];
        System.out.println(n);
        System.out.println(ge);
        System.out.println(avg);
    }
}
