package com.promptduel.service;

import com.promptduel.dto.response.EvaluationResultResponse;

public interface EvaluationService {

    /**
     * Run evaluation for all (version × input) combinations in a project.
     * Executes LLM calls in parallel. Each call is individually failure-isolated.
     *
     * @param projectId       the project to evaluate
     * @param userId          the authenticated user ID (for ownership check)
     * @param useJudgeScoring whether to enable LLM-as-Judge scoring (overrides config if non-null)
     * @return evaluation results with all runs and summary scores
     */
    EvaluationResultResponse runEvaluation(Long projectId, Long userId, Boolean useJudgeScoring);

    /**
     * Retrieve previously saved evaluation results for a project.
     */
    EvaluationResultResponse getResults(Long projectId, Long userId);
}
