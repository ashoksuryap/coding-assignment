package com.mckesson.codingassignment.rule;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.springframework.stereotype.Component;

/**
 * Validation rule related to alphanumerics
 */
@Component("ALPHA_NUMERIC")
public class AlphaNumericPasswordValidationRule implements PasswordValidationRule {

    private static final String VALIDATION_MESSAGE = "password must be a combination of alphanumeric characters";
    private static final String REGEX = "[\\p{Alnum}]*";


    /**
     * This method checks whether provided password has only letters and digits
     *
     * @param password - password to be validated
     * @throws PasswordValidationException if the password contains any character other than letter or digit
     */
    @Override
    public void validate(String password) {
        boolean foundMatch = password.matches(REGEX);
        if (!foundMatch) {
            throw new PasswordValidationException(VALIDATION_MESSAGE);
        }
    }
}
