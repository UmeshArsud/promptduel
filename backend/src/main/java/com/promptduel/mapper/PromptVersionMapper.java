package com.promptduel.mapper;

import com.promptduel.dto.response.PromptVersionResponse;
import com.promptduel.entity.PromptVersion;

public final class PromptVersionMapper {

    private PromptVersionMapper() {}

    public static PromptVersionResponse toResponse(PromptVersion version) {
        PromptVersionResponse response = new PromptVersionResponse();
        response.setId(version.getId());
        response.setProjectId(version.getProject().getId());
        response.setVersionLabel(version.getVersionLabel());
        response.setSystemPromptText(version.getSystemPromptText());
        response.setCreatedAt(version.getCreatedAt());
        return response;
    }
}
