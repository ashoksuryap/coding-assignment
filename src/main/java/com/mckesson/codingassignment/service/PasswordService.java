package com.mckesson.codingassignment.service;

import com.mckesson.codingassignment.rule.RuleResult;

import java.util.List;

public interface PasswordService {
    /**
     * This method validates password using the rules configured in system
     *
     * @param password - password to be validated
     * @return collection of applied rule result
     */
    List<RuleResult> validate(String password);
}
