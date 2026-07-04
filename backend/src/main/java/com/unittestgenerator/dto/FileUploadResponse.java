package com.unittestgenerator.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class FileUploadResponse {
    private String fileName;
    private String className;
    private String sourceCode;
    private List<String> methods;
    private String status;
    private String message;
}