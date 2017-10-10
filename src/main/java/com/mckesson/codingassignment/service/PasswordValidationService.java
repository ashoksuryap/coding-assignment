package com.mckesson.codingassignment.service;

import com.mckesson.codingassignment.exception.PasswordValidationException;

public interface PasswordValidationService {
    /**
     * This method validates password using the rules configured in system
     *
     * @param password - password to be validated
     * @throws PasswordValidationException if the password validation fails
     */
    void validate(String password);
}
