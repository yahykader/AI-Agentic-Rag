package com.example.ai_agentic.agent;


import com.example.ai_agentic.tools.AgentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class Agentic {
    private ChatClient chatClient;

    public Agentic(ChatClient.Builder chatClient, ChatMemory chatMemory, AgentTools agentTools, SimpleVectorStore vectorStore) {
        this.chatClient = chatClient.defaultAdvisors(
                new SimpleLoggerAdvisor(),
                MessageChatMemoryAdvisor.builder(chatMemory).build())
            .defaultTools(agentTools)
            //.defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
            .build();
    }

    public String askLLM(String query) {
        return this.chatClient.prompt().user(query).call().content();
    }

    public Flux<String> askAgentic(String query) {
        return this.chatClient.prompt().user(query).stream().content();
    }
}

