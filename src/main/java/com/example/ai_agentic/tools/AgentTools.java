package com.example.ai_agentic.tools;


import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AgentTools {

    private VectorStore vectorStore;


    public AgentTools(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Tool(
            name = "getEmployeeInfo",
            description = "Get employee information by name"
    )
    public EmployeeRecord getEmployeeInfo(@ToolParam(description = "Employer Name") String name) {
        return new EmployeeRecord(name, "Software Engineer", 75000);
    }


    @Tool(
            name = "searcherDocuments",
            description = "Search documents in the vector store du CV"
    )
    public List<String> searcherDocuments(@ToolParam(description = "Search Query") String query) {
        List<Document> documents= vectorStore.similaritySearch(SearchRequest.builder()
                        .query(query)
                        .topK(4)
                        .build());

        return documents.stream()
                .map(Document::getText)
                .collect(Collectors.toList());
    }


}
