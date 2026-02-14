package com.example.novels.controller;

import com.example.novels.dto.NovelDTO;
import com.example.novels.dto.PageRequestDTO;
import com.example.novels.dto.PageResultDTO;
import com.example.novels.service.NovelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
@RequestMapping("/api/novels")
public class NovelController {

    private final NovelService novelService;

    @Operation(summary = "novel 전체 조회", description = "novel 전체 조회 API")
    @GetMapping("")
    public PageResultDTO<NovelDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO {}", pageRequestDTO);
        PageResultDTO<NovelDTO> result = novelService.getList(pageRequestDTO);
        return result;
    }

    // /api/books/1
    @Operation(summary = "novel 상세조회", description = "novel 상세 조회 API")
    @GetMapping("/{id}")
    public NovelDTO getRow(@PathVariable Long id) {
        return novelService.getRow(id);
    }

    // /api/books/add
    @Operation(summary = "novel 추가", description = "novel 추가 조회 API")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/add")
    public Long postBook(@RequestBody NovelDTO novelDTO) {
        log.info("insert {}", novelDTO);

        return novelService.insert(novelDTO);
    }

    // /api/books/available/1
    // available 만 수정

    @Operation(summary = "novel 수정", description = "novel 수정 API - 이용 가능 여부")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PutMapping("/available/{id}")
    public Long putAvailable(@PathVariable Long id, @RequestBody NovelDTO novelDTO) {
        log.info("putAvailable {},{}", id, novelDTO);

        return novelService.availableUpdate(novelDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    public Long putBook(@PathVariable Long id, @RequestBody NovelDTO novelDTO) {
        log.info("edit {},{}", id, novelDTO);

        return novelService.update(novelDTO);
    }

    // /api/books/remove/1

    @Operation(summary = "novel 삭제", description = "novel 삭제 API")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/remove/{id}")
    public String deleteBook(@PathVariable Long id) {
        log.info("renive {}", id);
        novelService.remove(id);
        return "success";
    }

}
