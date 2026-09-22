package com.promptduel.service.impl;

import com.promptduel.dto.request.ProjectRequest;
import com.promptduel.dto.response.ProjectResponse;
import com.promptduel.entity.Project;
import com.promptduel.entity.User;
import com.promptduel.exception.ResourceNotFoundException;
import com.promptduel.exception.UnauthorizedAccessException;
import com.promptduel.mapper.ProjectMapper;
import com.promptduel.repository.ProjectRepository;
import com.promptduel.repository.UserRepository;
import com.promptduel.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getUserProjects(Long userId) {
        return projectRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long projectId, Long userId) {
        Project project = findProjectAndVerifyOwnership(projectId, userId);
        return ProjectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Project project = new Project(user, request.getTitle(), request.getDescription());
        project = projectRepository.save(project);
        return ProjectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long projectId, ProjectRequest request, Long userId) {
        Project project = findProjectAndVerifyOwnership(projectId, userId);
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project = projectRepository.save(project);
        return ProjectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId, Long userId) {
        Project project = findProjectAndVerifyOwnership(projectId, userId);
        projectRepository.delete(project);
    }

    /**
     * Helper: loads project by ID and checks ownership. Shared across all methods.
     */
    Project findProjectAndVerifyOwnership(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
        if (!project.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException();
        }
        return project;
    }
}
