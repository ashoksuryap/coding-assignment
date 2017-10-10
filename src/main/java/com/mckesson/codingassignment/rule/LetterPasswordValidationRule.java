package com.mckesson.codingassignment.rule;


import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.springframework.stereotype.Component;

/**
 * Validation rule related to letters in password
 */
@Component("LETTER")
public class LetterPasswordValidationRule implements PasswordValidationRule {

    private static final String VALIDATION_MESSAGE = "password must contain at least one letter and all letters should be in lower case";
    private static final String REGEX = "[^\\p{Upper}]*[\\p{Lower}]+[^\\p{Upper}]*";
    /**
     * This method checks whether provided password contains atleast one letter and all letters are in lower case
     *
     * @param password - password to be validated
     * @throws PasswordValidationException if the password does not contain any letter or letters in password are
     *                                     not in lower case
     */
    @Override
    public void validate(String password) {
        boolean foundMatch = password.matches(REGEX);
        if (!foundMatch) {
            throw new PasswordValidationException(VALIDATION_MESSAGE);
        }
    }
}
