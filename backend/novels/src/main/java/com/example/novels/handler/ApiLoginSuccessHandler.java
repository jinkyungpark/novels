package com.example.novels.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.novels.dto.MemberDTO;
import com.example.novels.utils.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApiLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        log.info("-----------------------------------------");
        log.info(authentication);
        log.info("-----------------------------------------");

        MemberDTO authUserDTO = (MemberDTO) authentication.getPrincipal();
        log.info("-------------- 회원 가입 후 넘어온 값 handler----------");
        log.info(authUserDTO);

        // Claim : 이름,값의 쌍
        Map<String, Object> claims = authUserDTO.getClaims();

        // 토큰 생성
        String accessTocken = JWTUtil.generateToken(claims, 10); // 10분
        String refreshToken = JWTUtil.generateToken(claims, 60 * 10); // 24시간

        claims.put("accessToken", accessTocken); // 나중에 구현
        claims.put("refreshToken", refreshToken);

        // 클라이언트에게 JSON 응답으로 토큰 전송
        Gson gson = new Gson();
        String jsonStr = gson.toJson(claims);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
    }

}
