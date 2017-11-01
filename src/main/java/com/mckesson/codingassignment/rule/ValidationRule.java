package com.mckesson.codingassignment.rule;

/**
 * Implement this interface to provide a new validation rule
 */
public interface ValidationRule {
    /**
     * This method validates input string
     *
     * @param str - string to be validated
     * @return RuleResult if the validation fails
     */
    RuleResult validate(String str);
}
