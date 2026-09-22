package com.promptduel.repository;

import com.promptduel.entity.PromptVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptVersionRepository extends JpaRepository<PromptVersion, Long> {

    List<PromptVersion> findByProjectIdOrderByCreatedAtAsc(Long projectId);

    long countByProjectId(Long projectId);
}
