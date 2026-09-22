package com.promptduel.mapper;

import com.promptduel.dto.response.TestRunResponse;
import com.promptduel.entity.TestRun;

public final class TestRunMapper {

    private TestRunMapper() {}

    public static TestRunResponse toResponse(TestRun run) {
        TestRunResponse response = new TestRunResponse();
        response.setId(run.getId());
        response.setPromptVersionId(run.getPromptVersion().getId());
        response.setVersionLabel(run.getPromptVersion().getVersionLabel());
        response.setTestInputId(run.getTestInput().getId());
        response.setInputText(run.getTestInput().getInputText());
        response.setModelOutput(run.getModelOutput());
        response.setScoreJson(run.getScoreJson());
        response.setLatencyMs(run.getLatencyMs());
        response.setStatus(run.getStatus().name());
        response.setErrorMessage(run.getErrorMessage());
        response.setCreatedAt(run.getCreatedAt());
        return response;
    }
}
