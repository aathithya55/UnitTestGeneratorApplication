package com.unittestgenerator.service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;
import com.unittestgenerator.dto.TestCaseInfo;
import com.unittestgenerator.dto.TestGenerationRequest;
import com.unittestgenerator.dto.TestGenerationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestGeneratorService {

    private final JavaParser javaParser = new JavaParser();

    public TestGenerationResponse generateTests(TestGenerationRequest request) {
        try {
            String sourceCode = request.getSourceCode();
            Optional<CompilationUnit> parseResult = javaParser.parse(sourceCode).getResult();

            if (parseResult.isEmpty()) {
                return TestGenerationResponse.builder()
                        .status("ERROR")
                        .message("Failed to parse Java source code")
                        .build();
            }

            CompilationUnit cu = parseResult.get();
            String className = request.getClassName();

            if (className == null || className.isEmpty()) {
                className = cu.getTypes().stream()
                        .findFirst()
                        .map(t -> t.getNameAsString())
                        .orElse("UnknownClass");
            }

            String testClassName = className + "Test";
            List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);
            List<TestCaseInfo> testCaseInfos = new ArrayList<>();
            StringBuilder testCode = new StringBuilder();

            // Generate imports
            testCode.append("import org.junit.jupiter.api.Test;\n");
            testCode.append("import org.junit.jupiter.api.BeforeEach;\n");
            testCode.append("import org.junit.jupiter.api.DisplayName;\n");
            testCode.append("import static org.junit.jupiter.api.Assertions.*;\n");
            testCode.append("import static org.mockito.Mockito.*;\n\n");

            // Generate class declaration
            testCode.append("public class ").append(testClassName).append(" {\n\n");
            testCode.append("    private ").append(className).append(" ")
                     .append(lowerFirst(className)).append(";\n\n");

            testCode.append("    @BeforeEach\n");
            testCode.append("    void setUp() {\n");
            testCode.append("        ").append(lowerFirst(className))
                     .append(" = new ").append(className).append("();\n");
            testCode.append("    }\n\n");

            int testCount = 0;
            for (MethodDeclaration method : methods) {
                if (method.isPrivate() || method.isStatic()) continue;

                String methodName = method.getNameAsString();
                List<Parameter> params = method.getParameters();
                Type returnType = method.getType();
                boolean isVoid = returnType.toString().equals("void");

                // Normal test
                testCount++;
                String testMethodName = methodName + "_ShouldReturnExpectedResult";
                testCode.append("    @Test\n");
                testCode.append("    @DisplayName(\"").append(methodName).append(" should return expected result\")\n");
                testCode.append("    void ").append(testMethodName).append("() {\n");
                testCode.append(generateMethodCall(className, methodName, params, isVoid, returnType.toString()));
                testCode.append("    }\n\n");

                testCaseInfos.add(TestCaseInfo.builder()
                        .methodName(testMethodName)
                        .description(methodName + " normal execution test")
                        .testType("NORMAL")
                        .expectedBehavior("Should execute successfully with valid inputs")
                        .build());

                // Edge case test
                if (request.isIncludeEdgeCases()) {
                    testCount++;
                    String edgeTestName = methodName + "_ShouldHandleEdgeCases";
                    testCode.append("    @Test\n");
                    testCode.append("    @DisplayName(\"").append(methodName).append(" should handle edge cases\")\n");
                    testCode.append("    void ").append(edgeTestName).append("() {\n");
                    testCode.append("        // Edge case: empty/null inputs\n");
                    testCode.append(generateEdgeCaseMethodCall(className, methodName, params, isVoid));
                    testCode.append("    }\n\n");

                    testCaseInfos.add(TestCaseInfo.builder()
                            .methodName(edgeTestName)
                            .description(methodName + " edge case handling")
                            .testType("EDGE_CASE")
                            .expectedBehavior("Should handle boundary/edge values gracefully")
                            .build());
                }

                // Negative test
                if (request.isIncludeNegativeTests()) {
                    testCount++;
                    String negTestName = methodName + "_ShouldThrowExceptionForInvalidInput";
                    testCode.append("    @Test\n");
                    testCode.append("    @DisplayName(\"").append(methodName).append(" should throw exception for invalid input\")\n");
                    testCode.append("    void ").append(negTestName).append("() {\n");
                    testCode.append(generateNegativeTest(className, methodName, params));
                    testCode.append("    }\n\n");

                    testCaseInfos.add(TestCaseInfo.builder()
                            .methodName(negTestName)
                            .description(methodName + " negative scenario test")
                            .testType("NEGATIVE")
                            .expectedBehavior("Should throw appropriate exception for invalid inputs")
                            .build());
                }
            }

            testCode.append("}\n");

            return TestGenerationResponse.builder()
                    .className(className)
                    .testClassName(testClassName)
                    .generatedCode(testCode.toString())
                    .testCases(testCaseInfos)
                    .totalTests(testCount)
                    .framework(request.getFramework())
                    .status("SUCCESS")
                    .message("Successfully generated " + testCount + " test cases")
                    .build();

        } catch (Exception e) {
            return TestGenerationResponse.builder()
                    .status("ERROR")
                    .message("Error generating tests: " + e.getMessage())
                    .build();
        }
    }

    private String generateMethodCall(String className, String methodName, 
                                       List<Parameter> params, boolean isVoid, String returnType) {
        StringBuilder sb = new StringBuilder();
        String instance = lowerFirst(className);

        StringBuilder args = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            Parameter p = params.get(i);
            args.append(getDefaultValue(p.getType().toString()));
            if (i < params.size() - 1) args.append(", ");
        }

        if (isVoid) {
            sb.append("        assertDoesNotThrow(() -> ")
              .append(instance).append(".").append(methodName)
              .append("(").append(args).append("));\n");
        } else {
            sb.append("        var result = ").append(instance).append(".")
              .append(methodName).append("(").append(args).append(");\n");
            sb.append("        assertNotNull(result);\n");
            if (returnType.equals("boolean") || returnType.equals("Boolean")) {
                sb.append("        // assertTrue(result); // TODO: Update expected value\n");
            } else if (returnType.equals("int") || returnType.equals("Integer") ||
                       returnType.equals("long") || returnType.equals("Long") ||
                       returnType.equals("double") || returnType.equals("Double")) {
                sb.append("        // assertEquals(expectedValue, result); // TODO: Update expected value\n");
            } else if (returnType.equals("String")) {
                sb.append("        // assertEquals(\"expected\", result); // TODO: Update expected value\n");
            }
        }
        return sb.toString();
    }

    private String generateEdgeCaseMethodCall(String className, String methodName, 
                                               List<Parameter> params, boolean isVoid) {
        StringBuilder sb = new StringBuilder();
        String instance = lowerFirst(className);

        StringBuilder args = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            Parameter p = params.get(i);
            args.append(getEdgeCaseValue(p.getType().toString()));
            if (i < params.size() - 1) args.append(", ");
        }

        if (isVoid) {
            sb.append("        assertDoesNotThrow(() -> ")
              .append(instance).append(".").append(methodName)
              .append("(").append(args).append("));\n");
        } else {
            sb.append("        var result = ").append(instance).append(".")
              .append(methodName).append("(").append(args).append(");\n");
            sb.append("        assertNotNull(result);\n");
        }
        return sb.toString();
    }

    private String generateNegativeTest(String className, String methodName, List<Parameter> params) {
        StringBuilder sb = new StringBuilder();
        String instance = lowerFirst(className);

        sb.append("        assertThrows(Exception.class, () -> {\n");
        sb.append("            ").append(instance).append(".")
          .append(methodName).append("(");

        for (int i = 0; i < params.size(); i++) {
            sb.append(getInvalidValue(params.get(i).getType().toString()));
            if (i < params.size() - 1) sb.append(", ");
        }

        sb.append(");\n");
        sb.append("        });\n");
        return sb.toString();
    }

    private String getDefaultValue(String type) {
        return switch (type) {
            case "int", "Integer" -> "0";
            case "long", "Long" -> "0L";
            case "double", "Double" -> "0.0";
            case "float", "Float" -> "0.0f";
            case "boolean", "Boolean" -> "true";
            case "String" -> "\"test\"";
            case "char", "Character" -> "'a'";
            default -> "null";
        };
    }

    private String getEdgeCaseValue(String type) {
        return switch (type) {
            case "int", "Integer" -> "Integer.MAX_VALUE";
            case "long", "Long" -> "Long.MAX_VALUE";
            case "double", "Double" -> "Double.MAX_VALUE";
            case "float", "Float" -> "Float.MAX_VALUE";
            case "boolean", "Boolean" -> "false";
            case "String" -> "\"\"";
            case "char", "Character" -> "'\0'";
            default -> "null";
        };
    }

    private String getInvalidValue(String type) {
        return switch (type) {
            case "int", "Integer" -> "-1";
            case "long", "Long" -> "-1L";
            case "double", "Double" -> "-1.0";
            case "float", "Float" -> "-1.0f";
            case "boolean", "Boolean" -> "false";
            case "String" -> "null";
            default -> "null";
        };
    }

    private String lowerFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}
