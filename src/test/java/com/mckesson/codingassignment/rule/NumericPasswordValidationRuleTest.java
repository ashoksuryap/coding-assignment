package com.mckesson.codingassignment.rule;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NumericPasswordValidationRuleTest {

    private PasswordValidationRule passwordValidationRule = new NumericPasswordValidationRule();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testValidate(){
        passwordValidationRule.validate("test1234");
    }

    @Test
    public void testValidate_chars(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage("password must contain at least one number");

        passwordValidationRule.validate("test");
    }

    @Test
    public void testValidate_digits(){
        passwordValidationRule.validate("123");
    }

    @Test
    public void testValidate_specialChars(){
        passwordValidationRule.validate("test@$3");
    }
}
