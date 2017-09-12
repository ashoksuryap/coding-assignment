package com.mckesson.codingassignment.service;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import com.mckesson.codingassignment.rule.PasswordValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    private Map<String, PasswordValidationRule> passwordValidationRules;

    @Value("#{'${password.validation.rules}'.split(',')}")
    private List<String> configuredRules;

    @Autowired
    public PasswordValidationServiceImpl(Map<String, PasswordValidationRule> passwordValidationRules) {
        this.passwordValidationRules = passwordValidationRules;
    }

    @Override
    public void validate(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new PasswordValidationException("password cannot be empty or null");
        }
        getConfiguredPasswordValidationRules().forEach(passwordValidationRule -> passwordValidationRule.validate(password));
    }

    private List<PasswordValidationRule> getConfiguredPasswordValidationRules() {
        List<String> configuredRulesList = getConfiguredRules();
        if (CollectionUtils.isEmpty(configuredRulesList)) {
            return new ArrayList<>(passwordValidationRules.values());
        }
        return configuredRulesList.stream().map(configuredRule -> passwordValidationRules.get(configuredRule)).collect(Collectors.toList());
    }

    public List<String> getConfiguredRules() {
        if (CollectionUtils.isEmpty(configuredRules)) return Collections.EMPTY_LIST;
        List<String> configuredRulesList = configuredRules.stream().filter(configuredRule -> !StringUtils.isEmpty(configuredRule)).collect(Collectors.toList());
        validateConfiguredRules(configuredRulesList);
        return configuredRulesList;
    }

    private void validateConfiguredRules(List<String> configuredRulesList) {
        configuredRulesList.forEach(configuredRule -> isValidRule(configuredRule));
    }

    private void isValidRule(String configuredRule) {
        if(passwordValidationRules.get(configuredRule) == null){
            throw new PasswordValidationException("configured password validation rule "+ configuredRule +" is not available in system. Please correct it.");
        }
    }

    public void setConfiguredRules(List<String> configuredRules) {
        this.configuredRules = configuredRules;
    }
}
