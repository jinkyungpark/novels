package com.example.novels.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.novels.dto.MemberDTO;
import com.example.novels.dto.RegisterDTO;
import com.example.novels.entity.Member;
import com.example.novels.entity.constant.MemberRole;
import com.example.novels.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인 처리
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("login test {}", username);

        Member member = memberRepository.getWithRoles(username);

        if (member == null)
            throw new UsernameNotFoundException("이메일 확인");

        // entity => dto
        MemberDTO memberDTO = new MemberDTO(member.getEmail(),
                member.getPw(), member.getNickname(), member.isSocial(),
                member.getMemberRoles().stream().map(role -> role.name()).collect(Collectors.toList()));

        log.info("찾은 멤버 {}", memberDTO);
        return memberDTO;
    }

    // 회원가입
    public void register(RegisterDTO dto) throws IllegalStateException {

        // 중복 이메일 확인
        Optional<Member> result = memberRepository.findById(dto.getEmail());
        if (result.isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .social(false)
                .pw(passwordEncoder.encode(dto.getPw()))
                .build();
        member.addRole(MemberRole.USER);
        memberRepository.save(member);
    }

}
