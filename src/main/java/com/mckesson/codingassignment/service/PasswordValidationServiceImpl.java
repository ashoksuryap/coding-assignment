package com.mckesson.codingassignment.service;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import com.mckesson.codingassignment.rule.PasswordValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
public class PasswordValidationServiceImpl implements PasswordValidationService {

    List<PasswordValidationRule> configuredPasswordValidationRules = new ArrayList<>();

    private static final String INVALID_RULE_MESSAGE = "configured password validation rule {0} is not available in system.";

    /**
     *
     * @param passwordValidationRules - All available rules
     * @param configuredRules - Rules defined in configuration file
     */
    @Autowired
    public PasswordValidationServiceImpl(Map<String, PasswordValidationRule> passwordValidationRules,
                                         @Value("#{'${password.validation.rules:}'.split(',')}") List<String> configuredRules) {
        List<String> configuredRulesList = filterNonEmptyConfiguredRules(configuredRules);
        validateConfiguredRules(configuredRulesList, passwordValidationRules);
        populateConfiguredPasswordValidationRules(passwordValidationRules, configuredRulesList);
    }

    private List<String> filterNonEmptyConfiguredRules(List<String> configuredRules) {
        List<String> configuredRulesList = Collections.EMPTY_LIST;
        if (!CollectionUtils.isEmpty(configuredRules)) {
            configuredRulesList = configuredRules.stream().filter(configuredRule -> !StringUtils.isEmpty(configuredRule)).collect(Collectors.toList());
        }
        return configuredRulesList;
    }

    private void validateConfiguredRules(List<String> configuredRulesList, Map<String, PasswordValidationRule> passwordValidationRules) {
        configuredRulesList.forEach(configuredRule -> isValidRule(configuredRule, passwordValidationRules));
    }

    private void isValidRule(String configuredRule, Map<String, PasswordValidationRule> passwordValidationRules) {
        if (passwordValidationRules.get(configuredRule) == null) {
            throw new PasswordValidationException(MessageFormat.format(INVALID_RULE_MESSAGE, configuredRule));
        }
    }

    private void populateConfiguredPasswordValidationRules(Map<String, PasswordValidationRule> passwordValidationRules, List<String> configuredRulesList) {
        if (CollectionUtils.isEmpty(configuredRulesList)) {
            configuredPasswordValidationRules.addAll(passwordValidationRules.values());
        } else {
            configuredRulesList.stream().map(configuredRule -> passwordValidationRules.get(configuredRule)).forEach(configuredPasswordValidationRules::add);
        }
    }

    @Override
    public void validate(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new PasswordValidationException("password cannot be empty or null");
        }
        configuredPasswordValidationRules.forEach(passwordValidationRule -> passwordValidationRule.validate(password));
    }
}
