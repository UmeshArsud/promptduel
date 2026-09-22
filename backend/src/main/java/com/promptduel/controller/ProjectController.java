package com.promptduel.controller;

import com.promptduel.dto.request.ProjectRequest;
import com.promptduel.dto.response.ProjectResponse;
import com.promptduel.service.ProjectService;
import com.promptduel.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final SecurityUtil securityUtil;

    public ProjectController(ProjectService projectService, SecurityUtil securityUtil) {
        this.projectService = projectService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getUserProjects() {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(projectService.getProject(id, userId));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        ProjectResponse response = projectService.createProject(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(projectService.updateProject(id, request, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        Long userId = securityUtil.getCurrentUserId();
        projectService.deleteProject(id, userId);
        return ResponseEntity.noContent().build();
    }
}
