package com.promptduel.llm;

/**
 * Abstraction for LLM API interactions.
 * Implementations can be swapped (e.g., GeminiLlmClient, OpenAiLlmClient)
 * without touching business logic.
 */
public interface LlmClient {

    /**
     * Sends a prompt to the LLM and returns the generated response.
     *
     * @param request the LLM request containing system prompt, user message, and config
     * @return the LLM response with generated text
     * @throws com.promptduel.exception.LlmApiException if the API call fails
     */
    LlmResponse generate(LlmRequest request);
}
