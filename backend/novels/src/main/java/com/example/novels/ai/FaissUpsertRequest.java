package com.example.novels.ai;

public record FaissUpsertRequest(long novelId, float[] embedding) {

}
