package com.example.ai_agentic;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiAgenticApplication {

    private EmbeddingModel embeddingModel;

	public static void main(String[] args) {
		SpringApplication.run(AiAgenticApplication.class, args);
	}


    @Bean
    public SimpleVectorStore vectorStore() {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        return vectorStore;
    }

}
