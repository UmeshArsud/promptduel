package com.promptduel.entity;

import jakarta.persistence.*;

/**
 * Represents one evaluation result for a specific (PromptVersion × TestInput) combination.
 * Stores the LLM output, scoring breakdown, latency, and status (COMPLETED or FAILED).
 */
@Entity
@Table(name = "test_runs", indexes = {
    @Index(name = "idx_test_runs_prompt_version_id", columnList = "prompt_version_id"),
    @Index(name = "idx_test_runs_test_input_id", columnList = "test_input_id")
})
public class TestRun extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_version_id", nullable = false)
    private PromptVersion promptVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_input_id", nullable = false)
    private TestInput testInput;

    @Column(name = "model_output", columnDefinition = "TEXT")
    private String modelOutput;

    @Column(name = "score_json", columnDefinition = "TEXT")
    private String scoreJson;

    @Column(name = "latency_ms")
    private Long latencyMs;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestRunStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    // Constructors ---

    public TestRun() {}

    /**
     * Creates a successful test run.
     */
    public TestRun(PromptVersion promptVersion, TestInput testInput,
                   String modelOutput, String scoreJson, Long latencyMs) {
        this.promptVersion = promptVersion;
        this.testInput = testInput;
        this.modelOutput = modelOutput;
        this.scoreJson = scoreJson;
        this.latencyMs = latencyMs;
        this.status = TestRunStatus.COMPLETED;
    }

    /**
     * Creates a failed test run with error details.
     */
    public static TestRun failed(PromptVersion promptVersion, TestInput testInput,
                                  String errorMessage, Long latencyMs) {
        TestRun run = new TestRun();
        run.promptVersion = promptVersion;
        run.testInput = testInput;
        run.status = TestRunStatus.FAILED;
        run.errorMessage = errorMessage;
        run.latencyMs = latencyMs;
        return run;
    }

    // --- Getters & Setters ---

    public PromptVersion getPromptVersion() {
        return promptVersion;
    }

    public void setPromptVersion(PromptVersion promptVersion) {
        this.promptVersion = promptVersion;
    }

    public TestInput getTestInput() {
        return testInput;
    }

    public void setTestInput(TestInput testInput) {
        this.testInput = testInput;
    }

    public String getModelOutput() {
        return modelOutput;
    }

    public void setModelOutput(String modelOutput) {
        this.modelOutput = modelOutput;
    }

    public String getScoreJson() {
        return scoreJson;
    }

    public void setScoreJson(String scoreJson) {
        this.scoreJson = scoreJson;
    }

    public Long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(Long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public TestRunStatus getStatus() {
        return status;
    }

    public void setStatus(TestRunStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
