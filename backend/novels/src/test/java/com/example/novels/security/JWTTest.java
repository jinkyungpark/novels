package com.example.novels.security;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.novels.utils.JWTUtil;

@SpringBootTest
public class JWTTest {
    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore() {
        System.out.println("----- JWT Test -----");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() {
        String email = "user1@gmail.com";
        String str = JWTUtil.generateToken(
                Map.of("email", email, "name", "User1"), 10);

        System.out.println(str);
    }

    @Test
    public void testValidate() throws InterruptedException {
        String email = "user1@gmail.com";
        String str = JWTUtil.generateToken(
                Map.of("email", email, "name", "User1"), 10);

        Thread.sleep(5000); // 5초 대기

        Map<String, Object> claimMap = JWTUtil.validateToken(str);

        System.out.println(claimMap);
    }
}
