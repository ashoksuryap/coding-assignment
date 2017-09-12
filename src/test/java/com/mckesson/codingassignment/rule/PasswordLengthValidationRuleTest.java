package com.mckesson.codingassignment.rule;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PasswordLengthValidationRuleTest {

    private PasswordValidationRule passwordValidationRule = new PasswordLengthValidationRule();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testValidate(){
        passwordValidationRule.validate("test1234");
    }

    @Test
    public void testValidate_5chars(){
        passwordValidationRule.validate("test5");
    }

    @Test
    public void testValidate_lessThan5chars(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage("password length must be between 5 and 12 characters");

        passwordValidationRule.validate("test");
    }

    @Test
    public void testValidate_12chars(){
        passwordValidationRule.validate("test12345678");
    }

    @Test
    public void testValidate_greaterThan12chars(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage("password length must be between 5 and 12 characters");

        passwordValidationRule.validate("testing123456");
    }
}
