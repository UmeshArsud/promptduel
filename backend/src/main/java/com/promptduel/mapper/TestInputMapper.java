package com.promptduel.mapper;

import com.promptduel.dto.response.TestInputResponse;
import com.promptduel.entity.TestInput;

public final class TestInputMapper {

    private TestInputMapper() {}

    public static TestInputResponse toResponse(TestInput testInput) {
        TestInputResponse response = new TestInputResponse();
        response.setId(testInput.getId());
        response.setProjectId(testInput.getProject().getId());
        response.setInputText(testInput.getInputText());
        response.setExpectedKeywords(testInput.getExpectedKeywords());
        response.setCreatedAt(testInput.getCreatedAt());
        return response;
    }
}
