package com.promptduel.controller;

import com.promptduel.dto.request.RunEvaluationRequest;
import com.promptduel.dto.response.EvaluationResultResponse;
import com.promptduel.service.EvaluationService;
import com.promptduel.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}")
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final SecurityUtil securityUtil;

    public EvaluationController(EvaluationService evaluationService,
                                 SecurityUtil securityUtil) {
        this.evaluationService = evaluationService;
        this.securityUtil = securityUtil;
    }

    /**
     * Run evaluation for all (version × input) combinations.
     * Optionally override useJudgeScoring in the request body.
     */
    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResultResponse> runEvaluation(
            @PathVariable Long projectId,
            @RequestBody(required = false) RunEvaluationRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        Boolean useJudge = request != null ? request.getUseJudgeScoring() : null;
        EvaluationResultResponse response =
                evaluationService.runEvaluation(projectId, userId, useJudge);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve previously saved evaluation results.
     */
    @GetMapping("/results")
    public ResponseEntity<EvaluationResultResponse> getResults(
            @PathVariable Long projectId) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(evaluationService.getResults(projectId, userId));
    }
}
