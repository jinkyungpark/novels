package com.example.novels.ai;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.novels.entity.Novel;
import com.example.novels.repository.NovelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class NovelAiEnrichmentListener {

    private final NovelRepository novelRepository;
    private final NovelTagRepository novelTagRepository;
    private final NovelAiEnrichmentService novelAiEnrichmentService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void ingest(NovelSavedEvent event) {

        log.info("왜 안들어와 ");
        // 저장된 Novel 찾아오기
        Novel novel = novelRepository.findById(event.novelId())
                .orElseThrow();

        try {

            // 1) LLM으로 소개문/태그 생성
            AiEnrichmentResult result = novelAiEnrichmentService.enrich(novel.getId());

            // 2) Novel.aiDescription 갱신
            novel.changeAiDescription(result.aiDescription());

            // dirtyChecking 안될 경우
            novelRepository.save(novel);

            // 3) 기존 태그 삭제 후 재생성(간단 운영)
            novelTagRepository.deleteByNovelId(novel.getId());

            List<NovelTag> tags = result.tags().stream()
                    .map(t -> {
                        NovelTag tag = new NovelTag();
                        tag.changeNovel(novel);
                        tag.changeType(t.type());
                        tag.changeValue(t.value());
                        tag.changeConfidence(t.confidence());
                        return tag;
                    })
                    .toList();

            novelTagRepository.saveAll(tags);

            // embedding 저장
            // 3) doc_text 생성 (추천/검색용 텍스트)
            // 예: title/author/genre/synopsis/aiDescription/tags 합치기
            // String docText = buildDocText(novel, tags);
            // 4) 임베딩 생성
            // novelEmbeddingService.embed(docText, novel);

        } catch (Exception e) {
            log.error("AI enrichment failed. novelId={}", novel.getId(), e); // ✅ 원인 예외 출력
        }
    }

    public static String buildDocText(Novel novel, List<NovelTag> tags) {
        String tagText = tags.stream()
                .map(t -> t.getType() + ":" + t.getValue())
                .collect(Collectors.joining(", "));

        return """
                [TITLE] %s
                [AUTHOR] %s
                [GENRE] %s
                [SYNOPSIS] %s
                [AI_DESCRIPTION] %s
                [TAGS] %s
                """.formatted(
                novel.getTitle(),
                novel.getAuthor(),
                novel.getGenre().getName(),
                String.valueOf(novel.getSynopsi()),
                String.valueOf(novel.getAiDescription()),
                tagText).strip();
    }

}