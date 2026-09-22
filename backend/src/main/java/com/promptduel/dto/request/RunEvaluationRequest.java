package com.promptduel.dto.request;

/**
 * Request body for triggering an evaluation run.
 * useJudgeScoring can override the global config per-request.
 */
public class RunEvaluationRequest {

    private Boolean useJudgeScoring;

    public RunEvaluationRequest() {}

    public RunEvaluationRequest(Boolean useJudgeScoring) {
        this.useJudgeScoring = useJudgeScoring;
    }

    public Boolean getUseJudgeScoring() {
        return useJudgeScoring;
    }

    public void setUseJudgeScoring(Boolean useJudgeScoring) {
        this.useJudgeScoring = useJudgeScoring;
    }
}
