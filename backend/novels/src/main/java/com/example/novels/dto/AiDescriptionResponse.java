package com.example.novels.dto;

public record AiDescriptionResponse(
        Long novelId,
        String aiDescription,
        boolean generated) {
}