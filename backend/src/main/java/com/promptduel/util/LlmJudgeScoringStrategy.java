package com.promptduel.util;

import com.promptduel.llm.LlmClient;
import com.promptduel.llm.LlmRequest;
import com.promptduel.llm.LlmResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Uses a second LLM call to judge the quality of the generated output.
 * Asks the model to rate the output 1-10 on accuracy, relevance, and tone.
 */
@Component
public class LlmJudgeScoringStrategy implements ScoringStrategy {

    private static final Logger log = LoggerFactory.getLogger(LlmJudgeScoringStrategy.class);
    private static final Pattern SCORE_PATTERN = Pattern.compile("\\b(\\d+(?:\\.\\d+)?)\\s*/\\s*10\\b");
    private static final Pattern SIMPLE_NUMBER_PATTERN = Pattern.compile("^\\s*(\\d+(?:\\.\\d+)?)\\s*$");

    private final LlmClient llmClient;

    public LlmJudgeScoringStrategy(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    @Override
    public Map<String, Object> score(String output, String expectedKeywords, String originalInput) {
        Map<String, Object> result = new LinkedHashMap<>();

        try {
            String judgePrompt = buildJudgePrompt(output, originalInput, expectedKeywords);

            LlmRequest request = LlmRequest.builder()
                    .systemPrompt("You are an expert evaluator. Rate the quality of AI-generated responses. " +
                            "Be concise. Respond with ONLY a single number from 1 to 10, followed by /10. " +
                            "Example: 7/10")
                    .userMessage(judgePrompt)
                    .maxTokens(50)
                    .temperature(0.1)
                    .build();

            LlmResponse response = llmClient.generate(request);
            double judgeScore = parseScore(response.getGeneratedText());

            result.put("judge_score", judgeScore);
            result.put("judge_raw_response", response.getGeneratedText().trim());

        } catch (Exception e) {
            log.warn("LLM judge scoring failed: {}", e.getMessage());
            result.put("judge_score", 0.0);
            result.put("judge_error", e.getMessage());
        }

        return result;
    }

    private String buildJudgePrompt(String output, String originalInput, String expectedKeywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("Rate the following AI response on a scale of 1-10 for accuracy, relevance, and quality.\n\n");
        sb.append("USER INPUT: ").append(originalInput).append("\n\n");
        if (expectedKeywords != null && !expectedKeywords.isBlank()) {
            sb.append("EXPECTED TOPICS/KEYWORDS: ").append(expectedKeywords).append("\n\n");
        }
        sb.append("AI RESPONSE:\n").append(output).append("\n\n");
        sb.append("Score (X/10):");
        return sb.toString();
    }

    private double parseScore(String response) {
        if (response == null || response.isBlank()) return 5.0;

        // Try X/10 pattern first
        Matcher matcher = SCORE_PATTERN.matcher(response);
        if (matcher.find()) {
            double score = Double.parseDouble(matcher.group(1));
            return Math.max(1.0, Math.min(10.0, score));
        }

        // Try plain number
        Matcher simpleMatcher = SIMPLE_NUMBER_PATTERN.matcher(response.trim());
        if (simpleMatcher.find()) {
            double score = Double.parseDouble(simpleMatcher.group(1));
            return Math.max(1.0, Math.min(10.0, score));
        }

        log.warn("Could not parse judge score from response: {}", response);
        return 5.0; // Default middle score if parsing fails
    }

    @Override
    public String getName() {
        return "judge";
    }
}
