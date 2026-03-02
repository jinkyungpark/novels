package com.example.novels.ai;

import java.util.List;

public record FaissSearchResponse(List<Item> results) {
    public record Item(Long novelId, double score) {
    }
}
