package com.mckesson.codingassignment.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

/**
 * Base class for password validation rules
 */
public abstract class AbstractValidationRule implements ValidationRule {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractValidationRule.class);
    private String ruleName;
    private String messageId;
    private Object[] messageParams;

    private MessageSource messageSource;

    AbstractValidationRule(String ruleName, MessageSource messageSource, String messageId, Object[] messageParams) {
        this.ruleName = ruleName;
        this.messageSource = messageSource;
        this.messageId = messageId;
        this.messageParams = messageParams;
    }

    abstract boolean doValidate(String str);

    @Override
    public RuleResult validate(String str) {
        LOGGER.debug("Validating password with {} validation rule", ruleName);
        boolean isSuccess = doValidate(str);
        LOGGER.debug("Result of {} validation rule is {}", ruleName, isSuccess ? "Success" : "Fail");
        RuleResult ruleResult = new RuleResult();
        ruleResult.setName(ruleName);
        ruleResult.setSuccess(isSuccess);
        if (!isSuccess) {
            String errorMessage = messageSource.getMessage(messageId, messageParams, null);
            ruleResult.setErrorMessage(errorMessage);
        }
        return ruleResult;
    }
}
