package com.example.novels.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.novels.dto.MemberDTO;
import com.example.novels.utils.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

// OncePerRequestFilter : 모든 요청에 대해 필터가 1회만 실행되도록 보장
@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    // /api/member/** 로 시작하는 경로는 필터를 거치지 않음
    // OPTIONS 메서드도 필터를 거치지 않음
    // /api/novels, /api/novels/{novelId} 도 필터를 거치지 않음
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        // Preflight 요청은 체크하지 않음
        if (request.getMethod().equals("OPTIONS"))
            return true;

        String path = request.getRequestURI();
        log.info("check uri " + path);

        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/api/member"))
            // /api/member/ 경로의 호출은 체크하지 않음
            return true;

        // /api/novels 로 시작하는 경로 제외
        if (path.equals("/api/novels") || path.matches("/api/novels/\\d+$")) //
            return true;

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("-------------- JWT Filter --------------");

        // 클라이언트는 쿠키나 세션을 이용하는 것이 불가능하므로 Request Header 의 Authorization 에
        // 토큰을 담아서 보낸다. => Authorization 헤더 추출
        String authHeaderStr = request.getHeader("Authorization");

        if (authHeaderStr != null && authHeaderStr.startsWith("Bearer ")) {
            try {
                // Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                // Bearer 토큰 문자열 분리를 위해 substring(7)
                String accessToken = authHeaderStr.substring(7);
                Map<String, Object> claims = JWTUtil.validateToken(accessToken);
                log.info("JWT claims " + claims);

                // 토큰을 통해 정보 추출
                String email = (String) claims.get("email");
                String password = (String) claims.get("password");
                String name = (String) claims.get("name");
                Boolean social = (Boolean) claims.get("social");
                List<String> roleNames = (List<String>) claims.get("roleNames");
                MemberDTO memberDTO = new MemberDTO(email, password, name,
                        social.booleanValue(), roleNames);

                log.info("====================================");
                log.info(memberDTO);
                log.info(memberDTO.getAuthorities());
                log.info("====================================");

                // 인증 정보를 SecurityContext 에 저장
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        memberDTO,
                        password, memberDTO.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response); // 통과

            } catch (Exception e) {
                log.error("JWT Check Error");
                log.error(e.getMessage());

                Gson gson = new Gson();
                String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

                response.setContentType("application/json");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(msg);
                printWriter.close();
            }
        }

    }

}
