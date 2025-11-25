package com.example.ai_agentic.controllers;


import com.example.ai_agentic.agent.Agentic;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin({"*"})
public class AgentController {
    private Agentic agentic;

    public AgentController(Agentic agentic) {
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
}

