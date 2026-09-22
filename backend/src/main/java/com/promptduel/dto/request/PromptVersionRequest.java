package com.promptduel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PromptVersionRequest {

    @Size(max = 50, message = "Version label must not exceed 50 characters")
    private String versionLabel;

    @NotBlank(message = "System prompt text is required")
    @Size(max = 50000, message = "System prompt text must not exceed 50000 characters")
    private String systemPromptText;

    public PromptVersionRequest() {}

    public PromptVersionRequest(String versionLabel, String systemPromptText) {
        this.versionLabel = versionLabel;
        this.systemPromptText = systemPromptText;
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
}
