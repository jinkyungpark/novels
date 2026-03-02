package com.example.novels.common;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;

/**
 * OpenAI API 설정
 */

@Log4j2
@Configuration
public class NovelAiConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        log.info("OpenAI API 클라이언트 생성");
        // return builder
        // .defaultSystem("You are a helpful assistant.")
        // .defaultOptions(OpenAiChatOptions.builder()
        // // .model("gpt-4o")
        // .temperature(0.7)
        // .maxTokens(150)
        // .build())
        // .build();
        return builder.build();
    }
}
