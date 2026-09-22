package com.promptduel.mapper;

import com.promptduel.dto.response.ProjectResponse;
import com.promptduel.entity.Project;

/**
 * Manual mapper for Project entity <-> DTO conversion.
 */
public final class ProjectMapper {

    private ProjectMapper() {}

    public static ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setPromptVersionCount(
                project.getPromptVersions() != null ? project.getPromptVersions().size() : 0);
        response.setTestInputCount(
                project.getTestInputs() != null ? project.getTestInputs().size() : 0);
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return response;
    }
}
