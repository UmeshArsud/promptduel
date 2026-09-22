package com.promptduel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ProjectResponse {

    private Long id;
    private String title;
    private String description;
    private int promptVersionCount;
    private int testInputCount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public ProjectResponse() {}

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPromptVersionCount() {
        return promptVersionCount;
    }

    public void setPromptVersionCount(int promptVersionCount) {
        this.promptVersionCount = promptVersionCount;
    }

    public int getTestInputCount() {
        return testInputCount;
    }

    public void setTestInputCount(int testInputCount) {
        this.testInputCount = testInputCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
