package com.mckesson.codingassignment.service;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import com.mckesson.codingassignment.rule.PasswordValidationRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Service
public class PasswordValidationServiceImpl implements PasswordValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordValidationServiceImpl.class);

    List<PasswordValidationRule> configuredPasswordValidationRules = new ArrayList<>();

    private static final String INVALID_RULE_MESSAGE = "configured password validation rule {0} is not available in system.";

    /**
     * @param passwordValidationRules - All available rules
     * @param configuredRules         - Rules defined in configuration file
     */
    @Autowired
    public PasswordValidationServiceImpl(Map<String, PasswordValidationRule> passwordValidationRules,
                                         @Value("#{'${password.validation.rules:}'.split(',')}") List<String> configuredRules) {
        configuredRules.stream()
                .filter(configuredRule -> !StringUtils.isEmpty(configuredRule))
                .filter(configuredRule -> isValidRule(configuredRule, passwordValidationRules))
                .map(configuredRule -> passwordValidationRules.get(configuredRule))
                .forEach(configuredPasswordValidationRules::add);
        if (CollectionUtils.isEmpty(configuredPasswordValidationRules)) {
            LOGGER.info("There are no configured rules. So, validating with all available rules.");
            configuredPasswordValidationRules.addAll(passwordValidationRules.values());
        }
    }

    private boolean isValidRule(String configuredRule, Map<String, PasswordValidationRule> passwordValidationRules) {
        if (passwordValidationRules.get(configuredRule) == null) {
            LOGGER.error(MessageFormat.format(INVALID_RULE_MESSAGE, configuredRule));
            throw new PasswordValidationException(MessageFormat.format(INVALID_RULE_MESSAGE, configuredRule));
        }
        return true;
    }

    @Override
    public void validate(String password) {
        if (StringUtils.isEmpty(password)) {
            LOGGER.warn("Password cannot be null or empty.");
            throw new PasswordValidationException("password cannot be empty or null");
        }
        configuredPasswordValidationRules.forEach(passwordValidationRule -> passwordValidationRule.validate(password));
    }
}
