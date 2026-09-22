package com.promptduel.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Aggregates scores from multiple ScoringStrategy implementations.
 * Computes a weighted overall score.
 *
 * When useJudgeScoring is false, the LlmJudgeScoringStrategy is skipped entirely
 * (no API call is made) and the overall score is computed from keyword + length only.
 */
@Component
public class CompositeScoringStrategy {

    private final KeywordScoringStrategy keywordStrategy;
    private final LengthScoringStrategy lengthStrategy;
    private final LlmJudgeScoringStrategy judgeStrategy;
    private final ObjectMapper objectMapper;

    // Weights when judge is enabled
    private static final double KEYWORD_WEIGHT_WITH_JUDGE = 0.30;
    private static final double LENGTH_WEIGHT_WITH_JUDGE = 0.20;
    private static final double JUDGE_WEIGHT = 0.50;

    // Weights when judge is disabled
    private static final double KEYWORD_WEIGHT_NO_JUDGE = 0.60;
    private static final double LENGTH_WEIGHT_NO_JUDGE = 0.40;

    public CompositeScoringStrategy(KeywordScoringStrategy keywordStrategy,
                                     LengthScoringStrategy lengthStrategy,
                                     LlmJudgeScoringStrategy judgeStrategy) {
        this.keywordStrategy = keywordStrategy;
        this.lengthStrategy = lengthStrategy;
        this.judgeStrategy = judgeStrategy;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Compute all scores and return a JSON string for storage.
     *
     * @param output           the LLM-generated text
     * @param expectedKeywords comma-separated keywords (may be null)
     * @param originalInput    the original user test input
     * @param useJudgeScoring  whether to invoke the LLM judge
     * @return JSON string containing all score dimensions and overall score
     */
    public String computeScores(String output, String expectedKeywords,
                                 String originalInput, boolean useJudgeScoring) {
        Map<String, Object> allScores = new LinkedHashMap<>();

        // Always run keyword and length strategies
        Map<String, Object> keywordScores = keywordStrategy.score(output, expectedKeywords, originalInput);
        Map<String, Object> lengthScores = lengthStrategy.score(output, expectedKeywords, originalInput);

        allScores.putAll(keywordScores);
        allScores.putAll(lengthScores);

        double keywordScore = ((Number) keywordScores.get("keyword_score")).doubleValue();
        double lengthScore = ((Number) lengthScores.get("length_score")).doubleValue();

        double overallScore;

        if (useJudgeScoring) {
            // Run the LLM judge
            Map<String, Object> judgeScores = judgeStrategy.score(output, expectedKeywords, originalInput);
            allScores.putAll(judgeScores);
            if (judgeScores.containsKey("judge_error")) {
                // Judge failed. Fallback to computing without judge score
                overallScore = (keywordScore * KEYWORD_WEIGHT_NO_JUDGE)
                             + (lengthScore * LENGTH_WEIGHT_NO_JUDGE);
            } else {
                double judgeScore = ((Number) judgeScores.get("judge_score")).doubleValue();
                overallScore = (keywordScore * KEYWORD_WEIGHT_WITH_JUDGE)
                             + (lengthScore * LENGTH_WEIGHT_WITH_JUDGE)
                             + (judgeScore * JUDGE_WEIGHT);
            }
        } else {
            // Skip judge entirely — no API call
            overallScore = (keywordScore * KEYWORD_WEIGHT_NO_JUDGE)
                         + (lengthScore * LENGTH_WEIGHT_NO_JUDGE);
        }

        allScores.put("overall_score", Math.round(overallScore * 100.0) / 100.0);
        allScores.put("judge_enabled", useJudgeScoring);

        try {
            return objectMapper.writeValueAsString(allScores);
        } catch (JsonProcessingException e) {
            return "{\"overall_score\": " + overallScore + ", \"error\": \"Failed to serialize scores\"}";
        }
    }
}
