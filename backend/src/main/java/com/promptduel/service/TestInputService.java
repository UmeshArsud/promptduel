package com.promptduel.service;

import com.promptduel.dto.request.TestInputRequest;
import com.promptduel.dto.response.TestInputResponse;

import java.util.List;

public interface TestInputService {

    List<TestInputResponse> getInputsByProject(Long projectId, Long userId);

    TestInputResponse getInput(Long projectId, Long inputId, Long userId);

    TestInputResponse createInput(Long projectId, TestInputRequest request, Long userId);

    TestInputResponse updateInput(Long projectId, Long inputId,
                                   TestInputRequest request, Long userId);

    void deleteInput(Long projectId, Long inputId, Long userId);
}
