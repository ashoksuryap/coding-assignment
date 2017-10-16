package com.mckesson.codingassignment.rule;


import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Validation rule related to letters and digits in password
 */
@Component("ALPHA_NUMERIC")
public class AlphaNumericPasswordValidationRule  implements PasswordValidationRule {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlphaNumericPasswordValidationRule.class);

    private static final String VALIDATION_MESSAGE = "password must consist of a mixture of lowercase letters and numerical digits only, with at least one of each";
    private static final String REGEX = "[\\p{Lower}]+[\\p{Digit}]+|[\\p{Digit}]+[\\p{Lower}]+";

    /**
     * This method checks whether provided password consist of a mixture of lowercase letters and numerical digits only, with at least one of each
     *
     * @param password - password to be validated
     * @throws PasswordValidationException if the password does not contain consist of a mixture of lowercase letters and numerical digits or at least one of each
     */
    @Override
    public void validate(String password) {
        LOGGER.info("Validating password with alphaNumeric password validation rule");
        boolean foundMatch = password.matches(REGEX);
        if (!foundMatch) {
            LOGGER.warn(VALIDATION_MESSAGE);
            throw new PasswordValidationException(VALIDATION_MESSAGE);
        }
    }
}
