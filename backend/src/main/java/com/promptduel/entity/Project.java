package com.promptduel.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a prompt evaluation project owned by a user.
 * Contains prompt versions, test inputs, and their evaluation results.
 */
@Entity
@Table(name = "projects", indexes = {
    @Index(name = "idx_projects_user_id", columnList = "user_id")
})
public class Project extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<PromptVersion> promptVersions = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<TestInput> testInputs = new ArrayList<>();

    // --- Constructors ---

    public Project() {}

    public Project(User user, String title, String description) {
        this.user = user;
        this.title = title;
        this.description = description;
    }

    // --- Getters & Setters ---

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PromptVersion> getPromptVersions() {
        return promptVersions;
    }

    public void setPromptVersions(List<PromptVersion> promptVersions) {
        this.promptVersions = promptVersions;
    }

    public List<TestInput> getTestInputs() {
        return testInputs;
    }

    public void setTestInputs(List<TestInput> testInputs) {
        this.testInputs = testInputs;
    }
}
