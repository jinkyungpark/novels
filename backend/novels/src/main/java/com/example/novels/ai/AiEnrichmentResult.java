package com.example.novels.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// 결과 스키마 DTO
public record AiEnrichmentResult(
        @JsonProperty(required = true, value = "aiDescription") String aiDescription,

        @JsonProperty(required = true, value = "tags") List<TagItem> tags) {
    public record TagItem(
            @JsonProperty(required = true, value = "type") TagType type,
            @JsonProperty(required = true, value = "value") String value,
            @JsonProperty(required = true, value = "confidence") Double confidence) {
    }
}
