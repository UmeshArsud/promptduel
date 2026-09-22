package com.promptduel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a user attempts to access/modify a resource they do not own.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException() {
        super("You do not have permission to access this resource");
    }
}
