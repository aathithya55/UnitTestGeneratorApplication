package com.unittestgenerator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestCaseInfo {
    private String methodName;
    private String description;
    private String testType;
    private String expectedBehavior;
}