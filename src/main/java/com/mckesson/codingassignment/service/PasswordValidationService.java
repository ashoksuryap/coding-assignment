package com.mckesson.codingassignment.service;

public interface PasswordValidationService {
    /**
     * This method validates password using the rules configured in system
     *
     * @param password - password to be validated
     */
    void validate(String password);
}
