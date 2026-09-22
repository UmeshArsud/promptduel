package com.promptduel.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a test input (sample user message) used to evaluate prompt versions.
 * Contains optional expected keywords for keyword-based scoring.
 */
@Entity
@Table(name = "test_inputs", indexes = {
    @Index(name = "idx_test_inputs_project_id", columnList = "project_id")
})
public class TestInput extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "input_text", nullable = false, columnDefinition = "TEXT")
    private String inputText;

    @Column(name = "expected_keywords", length = 1000)
    private String expectedKeywords;

    @OneToMany(mappedBy = "testInput", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestRun> testRuns = new ArrayList<>();

    // --- Constructors ---

    public TestInput() {}

    public TestInput(Project project, String inputText, String expectedKeywords) {
        this.project = project;
        this.inputText = inputText;
        this.expectedKeywords = expectedKeywords;
    }

    // --- Getters & Setters ---

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getExpectedKeywords() {
        return expectedKeywords;
    }

    public void setExpectedKeywords(String expectedKeywords) {
        this.expectedKeywords = expectedKeywords;
    }

    public List<TestRun> getTestRuns() {
        return testRuns;
    }

    public void setTestRuns(List<TestRun> testRuns) {
        this.testRuns = testRuns;
    }
}
