package com.example.novels.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.novels.filter.JWTCheckFilter;
import com.example.novels.handler.ApiLoginFailHandler;
import com.example.novels.handler.ApiLoginSuccessHandler;
import com.example.novels.handler.CustomAccessDeniedHandler;
import com.example.novels.handler.CustomLoginSuccessHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
@EnableMethodSecurity // @PreAuthorize, @PostAuthorize 사용
@EnableWebSecurity
@Configuration
public class SecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices)
                        throws Exception {

                http.authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.GET, "/api/novels/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated());

                http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
                                .configurationSource(corsConfigurationSource()));
                // API 서버는 무상태가 기본
                http.sessionManagement(
                                sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                // // csrf 중지
                http.csrf(csrf -> csrf.disable());

                // // 로그인
                http.formLogin(config -> {
                        config.loginPage("/api/member/login");
                        config.successHandler(new ApiLoginSuccessHandler());
                        config.failureHandler(new ApiLoginFailHandler());
                });

                // 필터 지정
                http.addFilterBefore(jwtCheckFilter(),
                                UsernamePasswordAuthenticationFilter.class);

                // // 접근제한 시 경로
                // http.exceptionHandling(e -> e.accessDeniedHandler(new
                // CustomAccessDeniedHandler()));

                return http.build();
        }

        @Bean // = new 한 후 스프링 컨테이너가 관리
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        CustomLoginSuccessHandler successHandler() {
                return new CustomLoginSuccessHandler();
        }

        @Bean
        RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
                RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
                TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("myKey",
                                userDetailsService, encodingAlgorithm);
                rememberMeServices.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
                rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 7);
                return rememberMeServices;
        }

        // org.springframework.web.cors.CorsConfiguration;
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.addAllowedOriginPattern("*");
                configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        JWTCheckFilter jwtCheckFilter() {
                return new JWTCheckFilter();
        }

}
