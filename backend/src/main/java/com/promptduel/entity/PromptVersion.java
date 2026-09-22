package com.promptduel.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific version of a system prompt within a project.
 * Users create multiple versions (v1, v2, etc.) to compare side-by-side.
 */
@Entity
@Table(name = "prompt_versions", indexes = {
    @Index(name = "idx_prompt_versions_project_id", columnList = "project_id")
})
public class PromptVersion extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "version_label", nullable = false, length = 50)
    private String versionLabel;

    @Column(name = "system_prompt_text", nullable = false, columnDefinition = "TEXT")
    private String systemPromptText;

    @OneToMany(mappedBy = "promptVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestRun> testRuns = new ArrayList<>();

    // --- Constructors ---

    public PromptVersion() {}

    public PromptVersion(Project project, String versionLabel, String systemPromptText) {
        this.project = project;
        this.versionLabel = versionLabel;
        this.systemPromptText = systemPromptText;
    }

    // --- Getters & Setters ---

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getVersionLabel() {
        return versionLabel;
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
    }

    public String getSystemPromptText() {
        return systemPromptText;
    }

    public void setSystemPromptText(String systemPromptText) {
        this.systemPromptText = systemPromptText;
    }

    public List<TestRun> getTestRuns() {
        return testRuns;
    }

    public void setTestRuns(List<TestRun> testRuns) {
        this.testRuns = testRuns;
    }
}
