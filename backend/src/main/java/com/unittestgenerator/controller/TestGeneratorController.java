package com.unittestgenerator.controller;

import com.unittestgenerator.dto.FileUploadResponse;
import com.unittestgenerator.dto.TestGenerationRequest;
import com.unittestgenerator.dto.TestGenerationResponse;
import com.unittestgenerator.service.FileParserService;
import com.unittestgenerator.service.TestGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TestGeneratorController {

    private final TestGeneratorService testGeneratorService;
    private final FileParserService fileParserService;

    public TestGeneratorController(TestGeneratorService testGeneratorService, 
                                   FileParserService fileParserService) {
        this.testGeneratorService = testGeneratorService;
        this.fileParserService = fileParserService;
    }

    @PostMapping("/generate")
    public ResponseEntity<TestGenerationResponse> generateTests(
            @RequestBody TestGenerationRequest request) {
        TestGenerationResponse response = testGeneratorService.generateTests(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileParserService.parseFile(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Unit Test Generator API is running!");
    }
}