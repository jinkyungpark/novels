package com.example.novels.ai;

import org.springframework.ai.chat.prompt.PromptTemplate;
import java.util.Map;

public class NovelAiPromptFactory {

    // “JSON”이라는 단어를 반드시 넣어야 JSON 모드/구조화 출력에서 안전함
    // (OpenAI 쪽 가이드에서도 JSON 지시가 없으면 문제가 생길 수 있다고 안내) :contentReference[oaicite:3]{index=3}
    private static final String TEMPLATE = """
        당신은 출판 마케터 겸 편집자입니다.
        아래 도서 메타데이터를 바탕으로:
        1) 서점 상세페이지에 들어갈 소개문(aiDescription)을 한국어로 작성하고
        2) 추천을 위한 태그(tags)를 생성하세요.

        반드시 JSON으로만 출력하세요. (설명/마크다운/코드블록 금지)

        [장르 스타일 가이드]
        {styleGuide}

        [도서 메타데이터]
        - 제목: {title}
        - 저자: {author}
        - 장르: {genre}
        - 출판일: {publishedDate}
        - 비고: {note}

        [출력 요구]
        - aiDescription: 400~800자(공백 포함), 2~4문단, 스포일러 금지, 독자가 끌리는 “후킹 1문장” 포함
        - tags: 아래 타입별로 2~5개씩 생성(총 10~20개 권장)
        - MOOD, THEME, TROPE, SETTING, AUDIENCE
        - tag.value는 한국어 1~6단어의 짧은 구
        - confidence는 0.50~0.95 범위
        """;

    public static String render(
        String styleGuide,
        String title,
        String author,
        String genre,
        String publishedDate,
        String note
    ) {
        var pt = new PromptTemplate(TEMPLATE);
        return pt.render(Map.of(
            "styleGuide", styleGuide,
            "title", title,
            "author", author,
            "genre", genre,
            "publishedDate", publishedDate == null ? "" : publishedDate,
            "note", note == null ? "" : note
        ));
    }
}
