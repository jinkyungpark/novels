package com.example.novels.dto;

import java.time.LocalDate;

import org.springframework.data.repository.query.Param;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovelDTO {
    private Long id;
    private String title; // 도서 제목
    private String author; // 저자
    private LocalDate publishedDate; // 출판일
    private boolean available;

    @JsonProperty("genre")
    private Long gid; // 장르아이디
    private String genreName; // 장르명
    // 평점
    private Integer rating;

    // user 정보
    private String email;
}
