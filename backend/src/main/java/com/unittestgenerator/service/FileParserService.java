package com.unittestgenerator.service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.unittestgenerator.dto.FileUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileParserService {

    private final JavaParser javaParser = new JavaParser();

    public FileUploadResponse parseFile(MultipartFile file) {
        try {
            String sourceCode = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines().collect(Collectors.joining("
"));

            Optional<CompilationUnit> parseResult = javaParser.parse(sourceCode).getResult();

            if (parseResult.isEmpty()) {
                return FileUploadResponse.builder()
                        .status("ERROR")
                        .message("Failed to parse Java file")
                        .build();
            }

            CompilationUnit cu = parseResult.get();
            String className = cu.getTypes().stream()
                    .findFirst()
                    .map(t -> t.getNameAsString())
                    .orElse("UnknownClass");

            List<String> methods = new ArrayList<>();
            cu.findAll(MethodDeclaration.class).forEach(m -> {
                if (!m.isPrivate()) {
                    methods.add(m.getDeclarationAsString(false, false, false));
                }
            });

            return FileUploadResponse.builder()
                    .fileName(file.getOriginalFilename())
                    .className(className)
                    .sourceCode(sourceCode)
                    .methods(methods)
                    .status("SUCCESS")
                    .message("File parsed successfully. Found " + methods.size() + " methods.")
                    .build();

        } catch (Exception e) {
            return FileUploadResponse.builder()
                    .status("ERROR")
                    .message("Error parsing file: " + e.getMessage())
                    .build();
        }
    }
}