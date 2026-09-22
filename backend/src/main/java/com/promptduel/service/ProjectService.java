package com.promptduel.service;

import com.promptduel.dto.request.ProjectRequest;
import com.promptduel.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

    List<ProjectResponse> getUserProjects(Long userId);

    ProjectResponse getProject(Long projectId, Long userId);

    ProjectResponse createProject(ProjectRequest request, Long userId);

    ProjectResponse updateProject(Long projectId, ProjectRequest request, Long userId);

    void deleteProject(Long projectId, Long userId);
}
