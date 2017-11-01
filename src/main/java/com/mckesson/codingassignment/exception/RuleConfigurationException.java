package com.mckesson.codingassignment.exception;

/**
 * Exception thrown when password validation fails.
 */
public class RuleConfigurationException extends RuntimeException {

    public RuleConfigurationException(String message) {
        super(message);
    }
}