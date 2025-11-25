package com.example.ai_agentic.tools;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class AgentTools {
    @Tool(
            name = "getEmployeeInfo",
            description = "Get employee information by name"
    )
    public EmployeeRecord getEmployeeInfo(@ToolParam(description = "Employer Name") String name) {
        return new EmployeeRecord(name, "Software Engineer", 75000);
    }
}

