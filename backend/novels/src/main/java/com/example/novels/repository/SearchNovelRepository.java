package com.example.novels.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchNovelRepository {

    // 하나 가져오기
    Object[] getNovelById(Long id);

    // 전체 리스트 + 페이지 나누기 + 검색(장르 or title, author)
    Page<Object[]> list(Long genreId, String keyword, Pageable pageable);
}
