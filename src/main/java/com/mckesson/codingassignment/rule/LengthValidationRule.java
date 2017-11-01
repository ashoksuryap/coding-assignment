package com.mckesson.codingassignment.rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Validation rule related to password length
 */
@Component
public class LengthValidationRule extends AbstractValidationRule {

    private int passwordMinLength;
    private int passwordMaxLength;

    private static final String MESSAGE_ID = "length.validation";

    @Autowired
    public LengthValidationRule(@Value("${password.validation.min.length:5}") int passwordMinLength,
                                @Value("${password.validation.max.length:12}") int passwordMaxLength,
                                MessageSource messageSource) {
        super("LENGTH", messageSource, MESSAGE_ID, new Object[]{passwordMinLength, passwordMaxLength});
        this.passwordMinLength = passwordMinLength;
        this.passwordMaxLength = passwordMaxLength;
    }

    /**
     * This method checks whether provided string length is between min and max length
     *
     * @param str - string to be validated
     * @retun false if the input string length is not between min and max length
     */
    @Override
    public boolean doValidate(String str) {
        int passwordLength = str.length();
        return passwordLength >= passwordMinLength && passwordLength <= passwordMaxLength;
    }
}
