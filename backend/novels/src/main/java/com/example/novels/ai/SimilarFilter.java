package com.example.novels.ai;

public record SimilarFilter(
        String genre, // null이면 필터 안함
        Integer minAge, // null이면 필터 안함
        Boolean completed // null이면 필터 안함
) {

}
