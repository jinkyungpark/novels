package com.example.novels.controller;

import com.example.novels.dto.MemberDTO;
import com.example.novels.dto.NovelDTO;
import com.example.novels.dto.PageRequestDTO;
import com.example.novels.dto.PageResultDTO;
import com.example.novels.dto.RegisterDTO;
import com.example.novels.service.MemberDetailsService;
import com.example.novels.service.NovelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Response Novels", description = "Response Nevel API")
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class RegisterController {

    private final MemberDetailsService memberService;

    // User 를 상속받는 DTO를 사용하면 안됨
    @Operation(summary = "novel 회원 가입", description = "novel 회원 가입 API")
    @PostMapping("/register")
    public ResponseEntity<String> postRegister(@RequestBody RegisterDTO registerDto) {
        log.info("registerDto {}", registerDto);

        try {
            memberService.register(registerDto);
            return new ResponseEntity<String>("가입성공", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("이미 가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

    }

}
