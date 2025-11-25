package com.example.ai_agentic.controllers;


import com.example.ai_agentic.agent.Agentic;
import com.example.ai_agentic.rag.DocumentIndexor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import java.io.IOException;
@RestController
@CrossOrigin({"*"})
public class AgentController {
    private Agentic agentic;
    private DocumentIndexor documentIndexor;

    public AgentController(Agentic agentic, DocumentIndexor documentIndexor) {
        this.documentIndexor = documentIndexor;
        this.agentic = agentic;
    }

    @GetMapping(
            value = {"/ask"},
            produces = {"text/plain"}
    )
    public String askLLM(@RequestParam(defaultValue = "Hello") String query) {
        return this.agentic.askLLM(query);
    }

    @GetMapping(
            value = {"/askAgent"},
            produces = {"text/plain"}
    )
    public Flux<String> askAgentic(@RequestParam(defaultValue = "Hello") String query) {
        return this.agentic.askAgentic(query);
    }

    @PostMapping(
            value = {"/loadFile"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void loadFile(@RequestParam("file") MultipartFile file) throws IOException {
        this.documentIndexor.loadFile(file);
    }
}

