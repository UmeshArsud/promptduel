package com.promptduel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the external LLM API call fails (timeout, rate limit, server error).
 */
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class LlmApiException extends RuntimeException {

    public LlmApiException(String message) {
        super(message);
    }

    public LlmApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
