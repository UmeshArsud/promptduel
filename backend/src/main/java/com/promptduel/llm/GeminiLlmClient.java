package com.promptduel.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.promptduel.exception.LlmApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Concrete LLM client implementation for Google Gemini API.
 * Calls the generateContent endpoint with system instruction and user message.
 */
@Component
public class GeminiLlmClient implements LlmClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiLlmClient.class);

    private final String apiKey;
    private final String defaultModel;
    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeminiLlmClient(
            @Value("${app.llm.api-key}") String apiKey,
            @Value("${app.llm.model}") String defaultModel,
            @Value("${app.llm.base-url}") String baseUrl,
            @Value("${app.llm.timeout-seconds}") int timeoutSeconds) {
        this.apiKey = apiKey;
        this.defaultModel = defaultModel;
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();

        // Configure RestTemplate with timeout
        var factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(timeoutSeconds));
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public LlmResponse generate(LlmRequest request) {
        String model = request.getModel() != null ? request.getModel() : defaultModel;
        String url = baseUrl + "/models/" + model + ":generateContent?key=" + apiKey;

        try {
            String requestBody = buildRequestBody(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            log.debug("Calling Gemini API: model={}", model);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new LlmApiException("Gemini API returned status: " + response.getStatusCode());
            }

            return parseResponse(response.getBody(), model);

        } catch (LlmApiException e) {
            throw e;
        } catch (RestClientException e) {
            log.error("Gemini API call failed: {}", e.getMessage());
            throw new LlmApiException("Failed to call Gemini API: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error calling Gemini API", e);
            throw new LlmApiException("Unexpected error calling Gemini API: " + e.getMessage(), e);
        }
    }

    private String buildRequestBody(LlmRequest request) {
        try {
            ObjectNode root = objectMapper.createObjectNode();

            // System instruction
            if (request.getSystemPrompt() != null && !request.getSystemPrompt().isEmpty()) {
                ObjectNode systemInstruction = objectMapper.createObjectNode();
                ObjectNode systemPart = objectMapper.createObjectNode();
                systemPart.put("text", request.getSystemPrompt());
                ArrayNode systemParts = objectMapper.createArrayNode();
                systemParts.add(systemPart);
                systemInstruction.set("parts", systemParts);
                root.set("systemInstruction", systemInstruction);
            }

            // User message content
            ArrayNode contents = objectMapper.createArrayNode();
            ObjectNode content = objectMapper.createObjectNode();
            content.put("role", "user");
            ArrayNode parts = objectMapper.createArrayNode();
            ObjectNode part = objectMapper.createObjectNode();
            part.put("text", request.getUserMessage());
            parts.add(part);
            content.set("parts", parts);
            contents.add(content);
            root.set("contents", contents);

            // Generation config
            ObjectNode generationConfig = objectMapper.createObjectNode();
            generationConfig.put("maxOutputTokens", request.getMaxTokens());
            generationConfig.put("temperature", request.getTemperature());
            root.set("generationConfig", generationConfig);

            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            throw new LlmApiException("Failed to build Gemini request body: " + e.getMessage(), e);
        }
    }

    private LlmResponse parseResponse(String responseBody, String model) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);

            // Check for API-level errors
            if (root.has("error")) {
                String errorMessage = root.path("error").path("message").asText("Unknown API error");
                throw new LlmApiException("Gemini API error: " + errorMessage);
            }

            JsonNode candidates = root.path("candidates");
            if (!candidates.isArray() || candidates.isEmpty()) {
                throw new LlmApiException("Gemini API returned no candidates");
            }

            JsonNode firstCandidate = candidates.get(0);
            JsonNode contentParts = firstCandidate.path("content").path("parts");
            if (!contentParts.isArray() || contentParts.isEmpty()) {
                throw new LlmApiException("Gemini API candidate has no content parts");
            }

            String generatedText = contentParts.get(0).path("text").asText("");

            // Extract token count if available
            int tokenCount = root.path("usageMetadata")
                    .path("candidatesTokenCount").asInt(0);

            return new LlmResponse(generatedText, model, tokenCount);

        } catch (LlmApiException e) {
            throw e;
        } catch (Exception e) {
            throw new LlmApiException("Failed to parse Gemini response: " + e.getMessage(), e);
        }
    }
}
