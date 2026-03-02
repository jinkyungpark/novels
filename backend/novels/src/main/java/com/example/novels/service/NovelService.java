package com.example.novels.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi.EmbeddingModel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.novels.ai.AiEnrichmentResult;
import com.example.novels.ai.NovelAiEnrichmentService;
import com.example.novels.ai.NovelSavedEvent;
import com.example.novels.ai.NovelTag;
import com.example.novels.ai.NovelTagRepository;
import com.example.novels.dto.AiDescriptionResponse;
import com.example.novels.dto.NovelDTO;
import com.example.novels.dto.PageRequestDTO;
import com.example.novels.dto.PageResultDTO;
import com.example.novels.entity.Genre;
import com.example.novels.entity.Novel;
import com.example.novels.repository.GenreRepository;
import com.example.novels.repository.GradeRepository;
import com.example.novels.repository.MemberRepository;
import com.example.novels.repository.NovelRepository;

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

    private final NovelAiEnrichmentService novelAiEnrichmentService;

    private final ApplicationEventPublisher eventPublisher;

    private final ChatClient chatClient;

    public Long insert(NovelDTO novelDTO) {

        log.info("service {}", novelDTO);

        Genre genre = genreRepository.findById(novelDTO.getGid()).get();
        System.out.println("genre " + genre);
        // Member user = userRepository.findById("user1@gmail.com").get();

        Novel novel = Novel.builder()
                .title(novelDTO.getTitle())
                .author(novelDTO.getAuthor())
                .publishedDate(novelDTO.getPublishedDate())
                .synopsi(novelDTO.getSynopsi())
                .available(novelDTO.isAvailable())
                .genre(genre)
                .build();
        Novel savedNovel = novelRepository.save(novel);

        // Novel 저장 완료가 되면 AI 생성 트리거 호출
        eventPublisher.publishEvent(new NovelSavedEvent(savedNovel.getId()));

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

    // Transactional 안 붙인 이유는 소개글이 수정될 수도 있기 때문(처음에 작성성)
    public AiDescriptionResponse getAiDescription(Long id, boolean force) {

        Novel novel = novelRepository.findById(id).orElseThrow();

        // 가져온 novel 에 description 이 있는지 다시 확인

        // 이미 존재하고 강제 재생성이 아니면 그대로 반환
        if (!force && novel.getAiDescription() != null && !novel.getAiDescription().isBlank()) {
            return new AiDescriptionResponse(id, novel.getAiDescription(), force);
        }

        // ai 요청 들어가기
        // 환각 방지: "줄거리 단정 금지", "장르/타깃 중심 카피"
        String system = """
                너는 출판사 마케터 겸 카피라이터다.
                입력으로 제공된 정보(도서명, 작가, 장르, 출판일)만 활용한다.
                줄거리/등장인물/세계관/수상경력 등 제공되지 않은 사실을 절대로 지어내지 마라.
                대신 장르의 매력, 독서 경험, 기대 포인트, 추천 독자를 중심으로 소개문을 작성한다.
                """;

        String user = """
                도서명: %s
                작가: %s
                장르: %s
                출판일: %s

                출력 규칙:
                - 한국어 3~5문장
                - 첫 문장은 훅(흥미 유발)으로 시작
                - 마지막 문장은 '이런 독자에게 추천:'으로 시작하는 한 문장
                - 과장 광고처럼 느껴지지 않게 자연스럽게
                """.formatted(novel.getTitle(), novel.getAuthor(), novel.getGenre().getName(),
                novel.getPublishedDate());

        String aiText = chatClient.prompt()
                .system(system)
                .user(user)
                .call()
                .content();

        novel.changeAiDescription(aiText);

        return new AiDescriptionResponse(id, aiText, force);
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
                .aiDescription(novel.getAiDescription())
                .aiDescriptionUpdatedAt(novel.getAiDescriptionUpdatedAt())
                .gid(genre.getId())
                .genreName(genre.getName())
                .rating(rating != null ? rating.intValue() : 0)
                .build();
        return dto;
    }

}
