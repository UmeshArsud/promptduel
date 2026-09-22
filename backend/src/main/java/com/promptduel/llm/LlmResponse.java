package com.promptduel.llm;

/**
 * Response wrapper for LLM API calls.
 */
public class LlmResponse {

    private final String generatedText;
    private final String model;
    private final int tokenCount;

    public LlmResponse(String generatedText, String model, int tokenCount) {
        this.generatedText = generatedText;
        this.model = model;
        this.tokenCount = tokenCount;
    }

    public String getGeneratedText() {
        return generatedText;
    }

    public String getModel() {
        return model;
    }

    public int getTokenCount() {
        return tokenCount;
    }
}
