package com.example.novels.ai;

import java.util.Map;

public class GenreStyleGuide {
    private static final Map<String, String> STYLE = Map.ofEntries(
        Map.entry("Fiction", """
            - 톤: 이야기 중심, 인물의 감정과 사건 흐름 강조
            - 문체: 2~4문단, 갈등 → 전개 → 기대감 구조
            - 핵심: 독자가 “이 인물을 따라가고 싶게” 만들 것
            - 금지: 줄거리 요약 나열식 설명
            - 태그 힌트:
            MOOD: 몰입감, 감동, 긴장
            THEME: 성장, 관계, 선택
            TROPE: 운명적 만남, 비밀, 재회
            """),

        Map.entry("Non-fiction", """
            - 톤: 신뢰감 있고 명확한 정보 전달
            - 문체: 2~3문단, 문제 제기 → 해결 통찰 → 독자에게 주는 가치
            - 핵심: '이 책을 읽으면 무엇을 얻는가'를 분명히
            - 금지: 과장된 수사, 감성 과잉
            - 태그 힌트:
            MOOD: 통찰, 현실적, 실용적
            THEME: 자기계발, 사회이슈, 경험
            AUDIENCE: 성인, 직장인, 학생
            """),

        Map.entry("Fantasy", """
            - 톤: 웅장하거나 신비로운 분위기
            - 문체: 2~4문단, 세계관 키워드 2~3개 자연 삽입
            - 핵심: 세계관 + 능력/운명/모험 요소 암시
            - 금지: 설정 설명만 장황하게 나열
            - 태그 힌트:
            MOOD: 장대한, 신비로운, 긴장감
            THEME: 운명, 성장, 전쟁, 모험
            SETTING: 이세계, 마법, 왕국, 중세풍
            TROPE: 각성, 회귀, 길드, 예언
            """),

        Map.entry("Dystopian", """
            - 톤: 차갑고 긴장감 있는 분위기
            - 문체: 2~3문단, 세계 붕괴/통제/억압 구조 강조
            - 핵심: 사회 시스템의 문제와 개인의 저항 대비
            - 금지: 희망찬 결말 암시 과다
            - 태그 힌트:
            MOOD: 암울함, 긴장, 불안
            THEME: 통제, 감시, 생존, 저항
            SETTING: 미래사회, 폐허, 감시체계
            """),

        Map.entry("Romance", """
            - 톤: 감정선 중심, 설렘과 긴장 균형
            - 문체: 2~4문단, 관계의 갈등 → 감정의 변화 강조
            - 핵심: 두 인물의 감정 케미를 느끼게 할 것
            - 금지: 유치한 표현, 과도한 감탄사
            - 태그 힌트:
            MOOD: 설렘, 달달함, 애절함
            THEME: 사랑, 오해, 재회, 성장
            TROPE: 계약연애, 첫사랑, 재회, 라이벌
            AUDIENCE: 성인, 청소년
            """),

        Map.entry("Programming", """
            - 톤: 전문적이되 친절한 설명
            - 문체: 2~3문단, 문제 상황 → 해결 방법 → 실무 적용 가치
            - 핵심: '이 책으로 무엇을 만들 수 있는가' 강조
            - 금지: 추상적 철학적 설명
            - 태그 힌트:
            MOOD: 실용적, 명확함, 체계적
            THEME: 웹개발, 백엔드, AI, 알고리즘
            AUDIENCE: 개발자, 초급개발자, 실무자
            """),

        Map.entry("Philosophy", """
            - 톤: 사유적이고 차분한 분위기
            - 문체: 2~4문단, 질문 제기 → 사유 확장 → 독자에게 던지는 여운
            - 핵심: 단정적 결론보다 사유의 확장
            - 금지: 교과서식 정의 나열
            - 태그 힌트:
            MOOD: 사색적, 차분함, 지적
            THEME: 존재, 자유, 윤리, 인간
            AUDIENCE: 성인, 인문학 관심자
            """)
    );

    public static String guide(String genreName) {
        return STYLE.getOrDefault(genreName, """
            - 톤: 친절하고 선명하게
            - 문체: 2~3문단, 핵심 매력 3가지
            - 태그 힌트: mood/theme/trope/setting을 균형있게
            """);
    }
}
