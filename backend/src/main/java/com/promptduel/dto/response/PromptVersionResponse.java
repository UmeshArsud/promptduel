package com.promptduel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class PromptVersionResponse {

    private Long id;
    private Long projectId;
    private String versionLabel;
    private String systemPromptText;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public PromptVersionResponse() {}

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getVersionLabel() {
        return versionLabel;
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
    }

    public String getSystemPromptText() {
        return systemPromptText;
    }

    public void setSystemPromptText(String systemPromptText) {
        this.systemPromptText = systemPromptText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
