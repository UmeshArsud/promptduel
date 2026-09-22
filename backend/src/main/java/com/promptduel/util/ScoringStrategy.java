package com.promptduel.util;

import java.util.Map;

/**
 * Strategy interface for scoring LLM outputs.
 * Implementations provide different scoring mechanisms (keyword, length, LLM judge).
 * Demonstrates polymorphism — selected and combined at runtime.
 */
public interface ScoringStrategy {

    /**
     * Score the given LLM output.
     *
     * @param output          the generated text from the LLM
     * @param expectedKeywords comma-separated expected keywords (may be null/empty)
     * @param originalInput   the original user test input
     * @return a map of score dimensions (e.g., "keyword_score" -> 8.5)
     */
    Map<String, Object> score(String output, String expectedKeywords, String originalInput);

    /**
     * Returns the name of this scoring strategy.
     */
    String getName();
}
