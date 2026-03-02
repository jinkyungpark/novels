package com.example.novels.ai;

// 벡터 DB에서 검색해올 때
public record FaissSearchRequest(float[] embedding, int topK) {
}