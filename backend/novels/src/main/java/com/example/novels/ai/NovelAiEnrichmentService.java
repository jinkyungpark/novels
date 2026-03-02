package com.example.novels.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.novels.entity.Novel;
import com.example.novels.repository.NovelRepository;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class NovelAiEnrichmentService {

        private final ChatClient chatClient;
        // private final FaissClient faissClient;
        private final NovelRepository novelRepository;

        public AiEnrichmentResult enrich(Long id) {

                Novel novel = novelRepository.findById(id).get();

                String genreName = novel.getGenre().getName();
                String styleGuide = GenreStyleGuide.guide(genreName);

                String prompt = NovelAiPromptFactory.render(
                                styleGuide,
                                novel.getTitle(),
                                novel.getAuthor(),
                                genreName,
                                novel.getPublishedDate().toString(),
                                "현재 메타데이터 기반으로 그럴듯한 소개문을 작성하세요.");

                var converter = new BeanOutputConverter<>(AiEnrichmentResult.class);

                // converter.getFormat(): “이 JSON Schema를 따라라” 형태의 포맷 지시문을 제공
                // 이걸 프롬프트에 포함시키거나(아래처럼), 옵션으로 response-format을 켤 수 있어.
                // :contentReference[oaicite:5]{index=5}
                String schemaInstruction = converter.getFormat();
                String finalPrompt = prompt + "\n\n[JSON Schema]\n" + schemaInstruction;

                // ChatClient fluent API
                String raw = chatClient.prompt()
                                .user(finalPrompt)
                                .call()
                                .content();

                return converter.convert(raw);
        }

}
