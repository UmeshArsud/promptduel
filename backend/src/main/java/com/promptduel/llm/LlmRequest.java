package com.promptduel.llm;

/**
 * Immutable LLM request object — uses Builder pattern for clean construction.
 *
 * Example:
 *   LlmRequest.builder()
 *       .systemPrompt("You are a helpful assistant")
 *       .userMessage("Explain recursion")
 *       .model("gemini-2.5-flash")
 *       .maxTokens(1024)
 *       .temperature(0.7)
 *       .build();
 */
public class LlmRequest {

    private final String systemPrompt;
    private final String userMessage;
    private final String model;
    private final int maxTokens;
    private final double temperature;

    private LlmRequest(Builder builder) {
        this.systemPrompt = builder.systemPrompt;
        this.userMessage = builder.userMessage;
        this.model = builder.model;
        this.maxTokens = builder.maxTokens;
        this.temperature = builder.temperature;
    }

    public static Builder builder() {
        return new Builder();
    }

    // --- Getters (no setters — immutable) ---

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getModel() {
        return model;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }

    // --- Builder ---

    public static class Builder {
        private String systemPrompt = "";
        private String userMessage = "";
        private String model = null;
        private int maxTokens = 2048;
        private double temperature = 0.7;

        public Builder systemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
            return this;
        }

        public Builder userMessage(String userMessage) {
            this.userMessage = userMessage;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public LlmRequest build() {
            return new LlmRequest(this);
        }
    }
}
