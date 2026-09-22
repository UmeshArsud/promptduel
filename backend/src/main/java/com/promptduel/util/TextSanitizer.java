package com.promptduel.util;

import java.util.regex.Pattern;

/**
 * Sanitizes user-supplied text before sending to the LLM API.
 * Strips patterns that could be used for prompt injection.
 */
public final class TextSanitizer {

    private TextSanitizer() {}

    // Patterns that attempt to override system instructions
    private static final Pattern SYSTEM_OVERRIDE = Pattern.compile(
            "(?i)(ignore\\s+(all\\s+)?(previous|prior|above)\\s+(instructions|prompts|rules)|" +
            "you\\s+are\\s+now\\s+|" +
            "new\\s+instructions?:|" +
            "forget\\s+(all|everything|your)\\s+|" +
            "disregard\\s+(all|previous|prior)\\s+)",
            Pattern.CASE_INSENSITIVE);

    /**
     * Sanitizes input text for safe LLM consumption.
     * Removes potential prompt injection patterns and trims whitespace.
     *
     * @param input the raw user input text
     * @return sanitized text
     */
    public static String sanitize(String input) {
        if (input == null) return "";

        String sanitized = input.trim();

        // Remove potential system instruction override attempts
        sanitized = SYSTEM_OVERRIDE.matcher(sanitized).replaceAll("[filtered]");

        // Limit length to prevent abuse
        if (sanitized.length() > 10000) {
            sanitized = sanitized.substring(0, 10000);
        }

        return sanitized;
    }
}
