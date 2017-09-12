package com.mckesson.codingassignment.rule;


import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.springframework.stereotype.Component;

/**
 * Validation rule related to letters in password
 */
@Component("LETTER")
public class LetterPasswordValidationRule implements PasswordValidationRule {

    private static final String LETTER_MANDATORY_VALIDATION_MESSAGE = "password must contain at least one letter";
    private static final String LETTER_LOWER_CASE_VALIDATION_MESSAGE = "password must contain lower case letters";

    /**
     * This method checks whether provided password contains atleast one letter and all letters are in lower case
     *
     * @param password - password to be validated
     * @throws PasswordValidationException if the password does not contain any letter or letters in password are
     *                                     not in lower case
     */
    @Override
    public void validate(String password) {
        boolean foundALetter = password.chars().anyMatch(Character::isLetter);

        if (!foundALetter)
            throw new PasswordValidationException(LETTER_MANDATORY_VALIDATION_MESSAGE);

        boolean areLowerCaseLetters = password.chars().filter(Character::isLetter).allMatch(Character::isLowerCase);
        if (!areLowerCaseLetters)
            throw new PasswordValidationException(LETTER_LOWER_CASE_VALIDATION_MESSAGE);

    }
}
