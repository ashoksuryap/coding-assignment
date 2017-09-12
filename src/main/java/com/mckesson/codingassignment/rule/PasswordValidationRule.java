package com.mckesson.codingassignment.rule;

import com.mckesson.codingassignment.exception.PasswordValidationException;

/**
 * Implement this interface to provide a new validation rule
 */
public interface PasswordValidationRule {
    /**
     * This method validates password
     *
     * @param password - password to be validated
     * @throws PasswordValidationException if the password validation fails
     */
    void validate(String password);
}
