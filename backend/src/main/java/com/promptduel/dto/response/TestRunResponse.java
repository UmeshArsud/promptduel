package com.promptduel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TestRunResponse {

    private Long id;
    private Long promptVersionId;
    private String versionLabel;
    private Long testInputId;
    private String inputText;
    private String modelOutput;
    private String scoreJson;
    private Long latencyMs;
    private String status;
    private String errorMessage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public TestRunResponse() {}

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPromptVersionId() {
        return promptVersionId;
    }

    public void setPromptVersionId(Long promptVersionId) {
        this.promptVersionId = promptVersionId;
    }

    public String getVersionLabel() {
        return versionLabel;
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
    }

    public Long getTestInputId() {
        return testInputId;
    }

    public void setTestInputId(Long testInputId) {
        this.testInputId = testInputId;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getModelOutput() {
        return modelOutput;
    }

    public void setModelOutput(String modelOutput) {
        this.modelOutput = modelOutput;
    }

    public String getScoreJson() {
        return scoreJson;
    }

    public void setScoreJson(String scoreJson) {
        this.scoreJson = scoreJson;
    }

    public Long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(Long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
