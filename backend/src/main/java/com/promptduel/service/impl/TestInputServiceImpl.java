package com.promptduel.service.impl;

import com.promptduel.dto.request.TestInputRequest;
import com.promptduel.dto.response.TestInputResponse;
import com.promptduel.entity.Project;
import com.promptduel.entity.TestInput;
import com.promptduel.exception.ResourceNotFoundException;
import com.promptduel.mapper.TestInputMapper;
import com.promptduel.repository.TestInputRepository;
import com.promptduel.service.TestInputService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestInputServiceImpl implements TestInputService {

    private final TestInputRepository testInputRepository;
    private final ProjectServiceImpl projectService;

    public TestInputServiceImpl(TestInputRepository testInputRepository,
                                 ProjectServiceImpl projectService) {
        this.testInputRepository = testInputRepository;
        this.projectService = projectService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestInputResponse> getInputsByProject(Long projectId, Long userId) {
        projectService.findProjectAndVerifyOwnership(projectId, userId);
        return testInputRepository.findByProjectIdOrderByCreatedAtAsc(projectId)
                .stream()
                .map(TestInputMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TestInputResponse getInput(Long projectId, Long inputId, Long userId) {
        projectService.findProjectAndVerifyOwnership(projectId, userId);
        TestInput input = findInputOrThrow(inputId);
        return TestInputMapper.toResponse(input);
    }

    @Override
    @Transactional
    public TestInputResponse createInput(Long projectId,
                                          TestInputRequest request, Long userId) {
        Project project = projectService.findProjectAndVerifyOwnership(projectId, userId);
        TestInput input = new TestInput(project, request.getInputText(), request.getExpectedKeywords());
        input = testInputRepository.save(input);
        return TestInputMapper.toResponse(input);
    }

    @Override
    @Transactional
    public TestInputResponse updateInput(Long projectId, Long inputId,
                                          TestInputRequest request, Long userId) {
        projectService.findProjectAndVerifyOwnership(projectId, userId);
        TestInput input = findInputOrThrow(inputId);
        input.setInputText(request.getInputText());
        input.setExpectedKeywords(request.getExpectedKeywords());
        input = testInputRepository.save(input);
        return TestInputMapper.toResponse(input);
    }

    @Override
    @Transactional
    public void deleteInput(Long projectId, Long inputId, Long userId) {
        projectService.findProjectAndVerifyOwnership(projectId, userId);
        TestInput input = findInputOrThrow(inputId);
        testInputRepository.delete(input);
    }

    private TestInput findInputOrThrow(Long inputId) {
        return testInputRepository.findById(inputId)
                .orElseThrow(() -> new ResourceNotFoundException("TestInput", inputId));
    }
}
