package com.mckesson.codingassignment.service;

import com.mckesson.codingassignment.exception.RuleConfigurationException;
import com.mckesson.codingassignment.rule.ValidationRule;
import com.mckesson.codingassignment.rule.RuleResult;
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
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
public class PasswordServiceImpl implements PasswordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordServiceImpl.class);

    List<ValidationRule> configuredValidationRules = new ArrayList<>();

    private static final String INVALID_RULE_MESSAGE = "configured password validation rule {} is not available in system.";

    /**
     * @param passwordValidationRules - All available rules
     * @param configuredRules         - Rules defined in configuration file
     */
    @Autowired
    public PasswordServiceImpl(Map<String, ValidationRule> passwordValidationRules,
                               @Value("#{'${password.validation.rules:}'.split(',')}") List<String> configuredRules) {
        configuredRules.stream()
                .filter(configuredRule -> !StringUtils.isEmpty(configuredRule))
                .filter(configuredRule -> isValidRule(configuredRule, passwordValidationRules))
                .map(passwordValidationRules::get)
                .forEach(configuredValidationRules::add);
        if (CollectionUtils.isEmpty(configuredValidationRules)) {
            LOGGER.info("There are no configured rules. So, validating with all available rules.");
            configuredValidationRules.addAll(passwordValidationRules.values());
        }
    }

    private boolean isValidRule(String configuredRule, Map<String, ValidationRule> passwordValidationRules) {
        if (passwordValidationRules.get(configuredRule) == null) {
            String invalidRuleMessage = MessageFormat.format("configured password validation rule {0} is not available in system.", configuredRule);
            LOGGER.error(invalidRuleMessage);
            throw new RuleConfigurationException(invalidRuleMessage);
        }
        return true;
    }

    @Override
    public List<RuleResult> validate(String password) {
        if (StringUtils.isEmpty(password)) {
            LOGGER.warn("Password cannot be null or empty.");
            throw new IllegalArgumentException("password cannot be empty or null");
        }
        return configuredValidationRules.stream().map(passwordValidationRule -> passwordValidationRule.validate(password)).collect(Collectors.toList());
    }
}
