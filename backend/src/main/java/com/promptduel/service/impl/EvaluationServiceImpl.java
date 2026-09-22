package com.promptduel.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptduel.dto.response.EvaluationResultResponse;
import com.promptduel.dto.response.TestRunResponse;
import com.promptduel.entity.*;
import com.promptduel.llm.LlmClient;
import com.promptduel.llm.LlmRequest;
import com.promptduel.llm.LlmResponse;
import com.promptduel.mapper.TestRunMapper;
import com.promptduel.repository.PromptVersionRepository;
import com.promptduel.repository.TestInputRepository;
import com.promptduel.repository.TestRunRepository;
import com.promptduel.service.EvaluationService;
import com.promptduel.util.CompositeScoringStrategy;
import com.promptduel.util.TextSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Evaluation service with parallel LLM execution and per-call failure isolation.
 * Uses a fixed thread pool (default 5 threads) to run all (version × input) combinations
 * concurrently via CompletableFuture.
 */
@Service
public class EvaluationServiceImpl implements EvaluationService {

    private static final Logger log = LoggerFactory.getLogger(EvaluationServiceImpl.class);

    private final PromptVersionRepository versionRepository;
    private final TestInputRepository inputRepository;
    private final TestRunRepository runRepository;
    private final ProjectServiceImpl projectService;
    private final LlmClient llmClient;
    private final CompositeScoringStrategy scoringStrategy;
    private final ExecutorService llmExecutor;
    private final ObjectMapper objectMapper;
    private final boolean defaultUseJudgeScoring;

    public EvaluationServiceImpl(
            PromptVersionRepository versionRepository,
            TestInputRepository inputRepository,
            TestRunRepository runRepository,
            ProjectServiceImpl projectService,
            LlmClient llmClient,
            CompositeScoringStrategy scoringStrategy,
            @Qualifier("llmExecutor") ExecutorService llmExecutor,
            @Value("${app.llm.use-judge-scoring}") boolean defaultUseJudgeScoring) {
        this.versionRepository = versionRepository;
        this.inputRepository = inputRepository;
        this.runRepository = runRepository;
        this.projectService = projectService;
        this.llmClient = llmClient;
        this.scoringStrategy = scoringStrategy;
        this.llmExecutor = llmExecutor;
        this.objectMapper = new ObjectMapper();
        this.defaultUseJudgeScoring = defaultUseJudgeScoring;
    }

    @Override
    @Transactional
    public EvaluationResultResponse runEvaluation(Long projectId, Long userId,
                                                    Boolean useJudgeScoring) {
        // Verify ownership
        projectService.findProjectAndVerifyOwnership(projectId, userId);

        // Load all versions and inputs
        List<PromptVersion> versions = versionRepository
                .findByProjectIdOrderByCreatedAtAsc(projectId);
        List<TestInput> inputs = inputRepository
                .findByProjectIdOrderByCreatedAtAsc(projectId);

        if (versions.isEmpty()) {
            throw new IllegalArgumentException(
                    "Project has no prompt versions. Add at least one version before running evaluation.");
        }
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException(
                    "Project has no test inputs. Add at least one test input before running evaluation.");
        }

        boolean judgeEnabled = useJudgeScoring != null ? useJudgeScoring : defaultUseJudgeScoring;

        log.info("Starting evaluation: projectId={}, versions={}, inputs={}, judge={}, " +
                 "total_combinations={}",
                 projectId, versions.size(), inputs.size(), judgeEnabled,
                 versions.size() * inputs.size());

        // Submit all combinations as parallel CompletableFutures
        List<CompletableFuture<TestRun>> futures = new ArrayList<>();

        for (PromptVersion version : versions) {
            for (TestInput input : inputs) {
                CompletableFuture<TestRun> future = CompletableFuture.supplyAsync(
                        () -> executeSingleRun(version, input, judgeEnabled),
                        llmExecutor
                );
                futures.add(future);
            }
        }

        // Wait for all to complete (each has its own error handling)
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Collect results
        List<TestRun> runs = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // Persist all runs
        runs = runRepository.saveAll(runs);

        log.info("Evaluation complete: projectId={}, total={}, successful={}, failed={}",
                 projectId,
                 runs.size(),
                 runs.stream().filter(r -> r.getStatus() == TestRunStatus.COMPLETED).count(),
                 runs.stream().filter(r -> r.getStatus() == TestRunStatus.FAILED).count());

        return buildResponse(projectId, runs);
    }

    /**
     * Executes a single (version × input) LLM call with per-call failure isolation.
     * If the call fails, returns a FAILED TestRun instead of throwing.
     */
    private TestRun executeSingleRun(PromptVersion version, TestInput input,
                                      boolean judgeEnabled) {
        long startTime = System.currentTimeMillis();

        try {
            // Sanitize user input before sending to LLM
            String sanitizedInput = TextSanitizer.sanitize(input.getInputText());

            // Build LLM request
            LlmRequest request = LlmRequest.builder()
                    .systemPrompt(version.getSystemPromptText())
                    .userMessage(sanitizedInput)
                    .build();

            // Call LLM
            LlmResponse response = llmClient.generate(request);
            long latencyMs = System.currentTimeMillis() - startTime;

            // Score the output
            String scoreJson = scoringStrategy.computeScores(
                    response.getGeneratedText(),
                    input.getExpectedKeywords(),
                    input.getInputText(),
                    judgeEnabled
            );

            return new TestRun(version, input, response.getGeneratedText(),
                              scoreJson, latencyMs);

        } catch (Exception e) {
            // Per-call failure isolation: capture the error, don't propagate
            long latencyMs = System.currentTimeMillis() - startTime;
            log.error("LLM call failed for version={} input={}: {}",
                      version.getVersionLabel(), input.getId(), e.getMessage());

            return TestRun.failed(version, input,
                    "LLM call failed: " + e.getMessage(), latencyMs);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationResultResponse getResults(Long projectId, Long userId) {
        projectService.findProjectAndVerifyOwnership(projectId, userId);

        List<TestRun> runs = runRepository.findAllByProjectId(projectId);
        return buildResponse(projectId, runs);
    }

    private EvaluationResultResponse buildResponse(Long projectId, List<TestRun> runs) {
        EvaluationResultResponse response = new EvaluationResultResponse();
        response.setProjectId(projectId);
        response.setTotalRuns(runs.size());
        response.setSuccessfulRuns(
                (int) runs.stream().filter(r -> r.getStatus() == TestRunStatus.COMPLETED).count());
        response.setFailedRuns(
                (int) runs.stream().filter(r -> r.getStatus() == TestRunStatus.FAILED).count());
        response.setRuns(
                runs.stream().map(TestRunMapper::toResponse).collect(Collectors.toList()));

        // Compute average overall score per version (only from completed runs)
        Map<String, Double> avgScores = new LinkedHashMap<>();
        Map<String, List<Double>> scoresByVersion = new LinkedHashMap<>();

        for (TestRun run : runs) {
            if (run.getStatus() == TestRunStatus.COMPLETED && run.getScoreJson() != null) {
                String label = run.getPromptVersion().getVersionLabel();
                double overallScore = extractOverallScore(run.getScoreJson());
                scoresByVersion
                        .computeIfAbsent(label, k -> new ArrayList<>())
                        .add(overallScore);
            }
        }

        for (Map.Entry<String, List<Double>> entry : scoresByVersion.entrySet()) {
            double avg = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue).average().orElse(0.0);
            avgScores.put(entry.getKey(), Math.round(avg * 100.0) / 100.0);
        }

        response.setAverageScoresByVersion(avgScores);
        return response;
    }

    private double extractOverallScore(String scoreJson) {
        try {
            JsonNode node = objectMapper.readTree(scoreJson);
            return node.path("overall_score").asDouble(0.0);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
