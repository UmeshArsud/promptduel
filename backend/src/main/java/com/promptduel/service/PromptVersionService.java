package com.promptduel.service;

import com.promptduel.dto.request.PromptVersionRequest;
import com.promptduel.dto.response.PromptVersionResponse;

import java.util.List;

public interface PromptVersionService {

    List<PromptVersionResponse> getVersionsByProject(Long projectId, Long userId);

    PromptVersionResponse getVersion(Long projectId, Long versionId, Long userId);

    PromptVersionResponse createVersion(Long projectId, PromptVersionRequest request, Long userId);

    PromptVersionResponse updateVersion(Long projectId, Long versionId,
                                         PromptVersionRequest request, Long userId);

    void deleteVersion(Long projectId, Long versionId, Long userId);
}
