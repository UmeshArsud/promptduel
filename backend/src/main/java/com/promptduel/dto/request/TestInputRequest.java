package com.promptduel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TestInputRequest {

    @NotBlank(message = "Test input text is required")
    @Size(max = 10000, message = "Input text must not exceed 10000 characters")
    private String inputText;

    @Size(max = 1000, message = "Expected keywords must not exceed 1000 characters")
    private String expectedKeywords;

    public TestInputRequest() {}

    public TestInputRequest(String inputText, String expectedKeywords) {
        this.inputText = inputText;
        this.expectedKeywords = expectedKeywords;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getExpectedKeywords() {
        return expectedKeywords;
    }

    public void setExpectedKeywords(String expectedKeywords) {
        this.expectedKeywords = expectedKeywords;
    }
}
