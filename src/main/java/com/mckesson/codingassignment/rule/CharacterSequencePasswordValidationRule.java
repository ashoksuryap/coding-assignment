package com.mckesson.codingassignment.rule;


import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation rule related to character sequence
 */
@Component("CHAR_SEQUENCE")
public class CharacterSequencePasswordValidationRule implements PasswordValidationRule {

    private static final String VALIDATION_MESSAGE = "password must not contain any sequence of characters immediately followed by the same sequence";
    private Pattern passwordPattern = Pattern.compile("(\\p{Alnum}{2,})\\1");

    /**
     * This method checks whether provided password contains any sequence of characters immediately followed by the same sequence
     *
     * @param password - password to be validated
     * @throws PasswordValidationException if the password contain any sequence of characters immediately followed by the same sequence
     */
    @Override
    public void validate(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        if (matcher.find()) {
            throw new PasswordValidationException(VALIDATION_MESSAGE);
        }
    }
}
