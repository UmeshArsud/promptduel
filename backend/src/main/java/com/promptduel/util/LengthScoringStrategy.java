package com.promptduel.util;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Scores output based on response length.
 * - Too short (< 50 chars): penalized heavily
 * - Sweet spot (100-2000 chars): full score
 * - Too long (> 5000 chars): mild penalty
 * Scale: 0-10
 */
@Component
public class LengthScoringStrategy implements ScoringStrategy {

    private static final int MIN_GOOD = 100;
    private static final int MAX_GOOD = 2000;
    private static final int MIN_ACCEPTABLE = 50;
    private static final int MAX_ACCEPTABLE = 5000;

    @Override
    public Map<String, Object> score(String output, String expectedKeywords, String originalInput) {
        Map<String, Object> result = new LinkedHashMap<>();

        int length = output != null ? output.length() : 0;
        double score;

        if (length < MIN_ACCEPTABLE) {
            // Very short — likely incomplete
            score = Math.max(1.0, (double) length / MIN_ACCEPTABLE * 4.0);
        } else if (length < MIN_GOOD) {
            // Short but acceptable
            score = 4.0 + ((double) (length - MIN_ACCEPTABLE) / (MIN_GOOD - MIN_ACCEPTABLE)) * 3.0;
        } else if (length <= MAX_GOOD) {
            // Sweet spot
            score = 10.0;
        } else if (length <= MAX_ACCEPTABLE) {
            // Getting long
            score = 10.0 - ((double) (length - MAX_GOOD) / (MAX_ACCEPTABLE - MAX_GOOD)) * 3.0;
        } else {
            // Very long — verbose
            score = Math.max(4.0, 7.0 - ((double) (length - MAX_ACCEPTABLE) / 5000.0) * 3.0);
        }

        result.put("length_score", Math.round(score * 100.0) / 100.0);
        result.put("output_length", length);
        return result;
    }

    @Override
    public String getName() {
        return "length";
    }
}
