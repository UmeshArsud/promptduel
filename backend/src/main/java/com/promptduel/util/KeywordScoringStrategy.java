package com.promptduel.util;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Scores output based on the presence of expected keywords.
 * Score = (matched keywords / total expected keywords) * 10.
 * Returns 10.0 if no expected keywords are specified.
 */
@Component
public class KeywordScoringStrategy implements ScoringStrategy {

    @Override
    public Map<String, Object> score(String output, String expectedKeywords, String originalInput) {
        Map<String, Object> result = new LinkedHashMap<>();

        if (expectedKeywords == null || expectedKeywords.isBlank()) {
            result.put("keyword_score", 10.0);
            result.put("keywords_matched", 0);
            result.put("keywords_total", 0);
            return result;
        }

        String lowerOutput = output.toLowerCase();
        String[] keywords = expectedKeywords.split(",");
        int matched = 0;
        List<String> matchedKeywords = new ArrayList<>();
        List<String> missedKeywords = new ArrayList<>();

        for (String keyword : keywords) {
            String trimmed = keyword.trim().toLowerCase();
            if (!trimmed.isEmpty()) {
                if (lowerOutput.contains(trimmed)) {
                    matched++;
                    matchedKeywords.add(trimmed);
                } else {
                    missedKeywords.add(trimmed);
                }
            }
        }

        int total = matchedKeywords.size() + missedKeywords.size();
        double score = total > 0 ? ((double) matched / total) * 10.0 : 10.0;

        result.put("keyword_score", Math.round(score * 100.0) / 100.0);
        result.put("keywords_matched", matched);
        result.put("keywords_total", total);
        result.put("matched_keywords", matchedKeywords);
        result.put("missed_keywords", missedKeywords);
        return result;
    }

    @Override
    public String getName() {
        return "keyword";
    }
}
