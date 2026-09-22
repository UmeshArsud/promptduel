package com.promptduel.dto.response;

import java.util.List;
import java.util.Map;

/**
 * Top-level response for an evaluation run.
 * Contains results grouped by test input, plus summary stats per version.
 */
public class EvaluationResultResponse {

    private Long projectId;
    private int totalRuns;
    private int successfulRuns;
    private int failedRuns;
    private List<TestRunResponse> runs;
    private Map<String, Double> averageScoresByVersion;

    public EvaluationResultResponse() {}

    // --- Getters & Setters ---

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public int getSuccessfulRuns() {
        return successfulRuns;
    }

    public void setSuccessfulRuns(int successfulRuns) {
        this.successfulRuns = successfulRuns;
    }

    public int getFailedRuns() {
        return failedRuns;
    }

    public void setFailedRuns(int failedRuns) {
        this.failedRuns = failedRuns;
    }

    public List<TestRunResponse> getRuns() {
        return runs;
    }

    public void setRuns(List<TestRunResponse> runs) {
        this.runs = runs;
    }

    public Map<String, Double> getAverageScoresByVersion() {
        return averageScoresByVersion;
    }

    public void setAverageScoresByVersion(Map<String, Double> averageScoresByVersion) {
        this.averageScoresByVersion = averageScoresByVersion;
    }
}
