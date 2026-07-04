package com.unittestgenerator.dto;

import lombok.Data;

@Data
public class TestGenerationRequest {
    private String className;
    private String sourceCode;
    private String framework = "junit5";
    private boolean includeEdgeCases = true;
    private boolean includeBoundaryTests = true;
    private boolean includeNegativeTests = true;
}