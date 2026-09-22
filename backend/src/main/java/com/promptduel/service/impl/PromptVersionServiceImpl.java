package com.promptduel.service.impl;

import com.promptduel.dto.request.PromptVersionRequest;
import com.promptduel.dto.response.PromptVersionResponse;
import com.promptduel.entity.Project;
import com.promptduel.entity.PromptVersion;
import com.promptduel.exception.ResourceNotFoundException;
import com.promptduel.mapper.PromptVersionMapper;
import com.promptduel.repository.PromptVersionRepository;
import com.promptduel.service.PromptVersionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromptVersionServiceImpl implements PromptVersionService {

    private final PromptVersionRepository versionRepository;
    private final ProjectServiceImpl projectService;

    public PromptVersionServiceImpl(PromptVersionRepository versionRepository,
                                     ProjectServiceImpl projectService) {
        this.versionRepository = versionRepository;
        this.projectService = projectService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromptVersionResponse> getVersionsByProject(Long projectId, Long userId) {
        projectService.findProjectAndVerifyOwnership(projectId, userId);
        return versionRepository.findByProjectIdOrderByCreatedAtAsc(projectId)
                .stream()
                .map(PromptVersionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PromptVersionResponse getVersion(Long projectId, Long versionId, Long userId) {
        projectService.findProjectAndVerifyOwnership(projectId, userId);
        PromptVersion version = findVersionOrThrow(versionId);
        return PromptVersionMapper.toResponse(version);
    }

    @Override
    @Transactional
    public PromptVersionResponse createVersion(Long projectId,
                                                PromptVersionRequest request, Long userId) {
        Project project = projectService.findProjectAndVerifyOwnership(projectId, userId);

        String label = request.getVersionLabel();
        if (!StringUtils.hasText(label)) {
            long count = versionRepository.countByProjectId(projectId);
            label = "v" + (count + 1);
        }

        PromptVersion version = new PromptVersion(project, label, request.getSystemPromptText());
        version = versionRepository.save(version);
        return PromptVersionMapper.toResponse(version);
    }

    @Override
    @Transactional
    public PromptVersionResponse updateVersion(Long projectId, Long versionId,
                                                PromptVersionRequest request, Long userId) {
        projectService.findProjectAndVerifyOwnership(projectId, userId);
        PromptVersion version = findVersionOrThrow(versionId);

        if (StringUtils.hasText(request.getVersionLabel())) {
            version.setVersionLabel(request.getVersionLabel());
        }
        version.setSystemPromptText(request.getSystemPromptText());
        version = versionRepository.save(version);
        return PromptVersionMapper.toResponse(version);
    }

    @Override
    @Transactional
    public void deleteVersion(Long projectId, Long versionId, Long userId) {
        projectService.findProjectAndVerifyOwnership(projectId, userId);
        PromptVersion version = findVersionOrThrow(versionId);
        versionRepository.delete(version);
    }

    private PromptVersion findVersionOrThrow(Long versionId) {
        return versionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("PromptVersion", versionId));
    }
}
