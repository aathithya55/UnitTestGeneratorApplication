package com.unittestgenerator.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TestGenerationResponse {
    private String className;
    private String testClassName;
    private String generatedCode;
    private List<TestCaseInfo> testCases;
    private int totalTests;
    private String framework;
    private String status;
    private String message;
}