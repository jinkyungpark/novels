package com.example.novels.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.novels.dto.NovelDTO;
import com.example.novels.dto.PageRequestDTO;
import com.example.novels.dto.PageResultDTO;
import com.example.novels.entity.Genre;
import com.example.novels.entity.Grade;
import com.example.novels.entity.Novel;
import com.example.novels.entity.Member;
import com.example.novels.repository.GenreRepository;
import com.example.novels.repository.GradeRepository;
import com.example.novels.repository.NovelRepository;
import com.example.novels.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class NovelService {

    private final NovelRepository novelRepository;
    private final GradeRepository gradeRepository;
    private final GenreRepository genreRepository;
    private final MemberRepository userRepository;

    public Long insert(NovelDTO novelDTO) {

        log.info("service {}", novelDTO);

        Genre genre = genreRepository.findById(novelDTO.getGid()).get();
        System.out.println("genre " + genre);
        // Member user = userRepository.findById("user1@gmail.com").get();

        Novel novel = Novel.builder()
                .title(novelDTO.getTitle())
                .author(novelDTO.getAuthor())
                .publishedDate(novelDTO.getPublishedDate())
                .available(novelDTO.isAvailable())
                .genre(genre)
                .build();
        Novel savedNovel = novelRepository.save(novel);

        // Grade grade = Grade.builder()
        // .rating(novelDTO.getRating())
        // .user(user)
        // .novel(savedNovel)
        // .build();
        // gradeRepository.save(grade);

        return savedNovel.getId();
    }

    public Long update(NovelDTO novelDTO) {
        // 수정 가능한 정보는 available 와 장르 일 때
        Novel novel = novelRepository.findById(novelDTO.getId()).orElseThrow();

        novel.changeAvailable(novelDTO.isAvailable());
        novel.changeGenre(Genre.builder().id(novelDTO.getGid()).build());
        // novelRepository.save(novel);

        return novel.getId();
    }

    public Long availableUpdate(NovelDTO novelDTO) {
        // 수정 가능한 정보는 available 와 rating 일 때
        Novel novel = novelRepository.findById(novelDTO.getId()).orElseThrow();

        novel.changeAvailable(novelDTO.isAvailable());
        // novelRepository.save(novel);

        return novel.getId();
    }

    public void remove(Long id) {
        Novel novel = novelRepository.findById(id).get();

        // user 들이 넣은 도서 평점 삭제
        gradeRepository.deleteByNovel(novel);
        // 도서 삭제
        novelRepository.delete(novel);
    }

    @Transactional(readOnly = true)
    public NovelDTO getRow(Long id) {
        Object[] result = novelRepository.getNovelById(id);
        NovelDTO dto = entityToDto((Novel) result[0], (Genre) result[1], (Double) result[2]);
        return dto;
    }

    @Transactional(readOnly = true)
    public PageResultDTO<NovelDTO> getList(PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(requestDTO.getPage() - 1, requestDTO.getSize(), Sort.by("id").descending());

        Page<Object[]> result = novelRepository.list(requestDTO.getGenre(), requestDTO.getKeyword(), pageable);

        List<NovelDTO> dtoList = result.get().map(arr -> {
            Novel novel = (Novel) arr[0];
            Genre genre = (Genre) arr[1];
            Double rating = (Double) arr[2];

            NovelDTO novelDTO = NovelDTO.builder()
                    .id(novel.getId())
                    .title(novel.getTitle())
                    .author(novel.getAuthor())
                    .publishedDate(novel.getPublishedDate())
                    .available(novel.isAvailable())
                    .gid(genre.getId())
                    .genreName(genre.getName())
                    .rating(rating != null ? rating.intValue() : 0)
                    .build();
            return novelDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResultDTO.<NovelDTO>withAll().dtoList(dtoList).totalCount(totalCount)
                .pageRequestDTO(requestDTO).build();
    }

    // Entity --> DTO 로 변환
    private NovelDTO entityToDto(Novel novel, Genre genre, Double rating) {

        NovelDTO dto = NovelDTO.builder()
                .id(novel.getId())
                .title(novel.getTitle())
                .author(novel.getAuthor())
                .publishedDate(novel.getPublishedDate())
                .available(novel.isAvailable())
                .gid(genre.getId())
                .genreName(genre.getName())
                .rating(rating != null ? rating.intValue() : 0)
                .build();
        return dto;
    }

}
