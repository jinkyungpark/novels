package com.example.novels.ai;

import java.util.List;

public record SimilarNovelResponse(
        Long id,
        String title,
        String author,
        String genre,
        List<String> tags,
        float score) {
}
