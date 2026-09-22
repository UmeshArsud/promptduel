package com.promptduel.controller;

import com.promptduel.dto.request.PromptVersionRequest;
import com.promptduel.dto.response.PromptVersionResponse;
import com.promptduel.service.PromptVersionService;
import com.promptduel.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/versions")
public class PromptVersionController {

    private final PromptVersionService versionService;
    private final SecurityUtil securityUtil;

    public PromptVersionController(PromptVersionService versionService,
                                    SecurityUtil securityUtil) {
        this.versionService = versionService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public ResponseEntity<List<PromptVersionResponse>> getVersions(
            @PathVariable Long projectId) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(versionService.getVersionsByProject(projectId, userId));
    }

    @GetMapping("/{versionId}")
    public ResponseEntity<PromptVersionResponse> getVersion(
            @PathVariable Long projectId, @PathVariable Long versionId) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(versionService.getVersion(projectId, versionId, userId));
    }

    @PostMapping
    public ResponseEntity<PromptVersionResponse> createVersion(
            @PathVariable Long projectId,
            @Valid @RequestBody PromptVersionRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        PromptVersionResponse response = versionService.createVersion(projectId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{versionId}")
    public ResponseEntity<PromptVersionResponse> updateVersion(
            @PathVariable Long projectId, @PathVariable Long versionId,
            @Valid @RequestBody PromptVersionRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(
                versionService.updateVersion(projectId, versionId, request, userId));
    }

    @DeleteMapping("/{versionId}")
    public ResponseEntity<Void> deleteVersion(
            @PathVariable Long projectId, @PathVariable Long versionId) {
        Long userId = securityUtil.getCurrentUserId();
        versionService.deleteVersion(projectId, versionId, userId);
        return ResponseEntity.noContent().build();
    }
}
