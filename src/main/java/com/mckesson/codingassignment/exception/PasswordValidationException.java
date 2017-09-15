package com.mckesson.codingassignment.exception;

/**
 * Exception thrown when password validation fails.
 */
public class PasswordValidationException extends RuntimeException {

    public PasswordValidationException(String message) {
        super(message);
    }
}
