package com.promptduel.controller;

import com.promptduel.dto.request.TestInputRequest;
import com.promptduel.dto.response.TestInputResponse;
import com.promptduel.service.TestInputService;
import com.promptduel.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/inputs")
public class TestInputController {

    private final TestInputService testInputService;
    private final SecurityUtil securityUtil;

    public TestInputController(TestInputService testInputService,
                                SecurityUtil securityUtil) {
        this.testInputService = testInputService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public ResponseEntity<List<TestInputResponse>> getInputs(
            @PathVariable Long projectId) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(testInputService.getInputsByProject(projectId, userId));
    }

    @GetMapping("/{inputId}")
    public ResponseEntity<TestInputResponse> getInput(
            @PathVariable Long projectId, @PathVariable Long inputId) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(testInputService.getInput(projectId, inputId, userId));
    }

    @PostMapping
    public ResponseEntity<TestInputResponse> createInput(
            @PathVariable Long projectId,
            @Valid @RequestBody TestInputRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        TestInputResponse response = testInputService.createInput(projectId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{inputId}")
    public ResponseEntity<TestInputResponse> updateInput(
            @PathVariable Long projectId, @PathVariable Long inputId,
            @Valid @RequestBody TestInputRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(
                testInputService.updateInput(projectId, inputId, request, userId));
    }

    @DeleteMapping("/{inputId}")
    public ResponseEntity<Void> deleteInput(
            @PathVariable Long projectId, @PathVariable Long inputId) {
        Long userId = securityUtil.getCurrentUserId();
        testInputService.deleteInput(projectId, inputId, userId);
        return ResponseEntity.noContent().build();
    }
}
