package com.mckesson.codingassignment.rule;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlphaNumericPasswordValidationRuleTest {

    private PasswordValidationRule passwordValidationRule = new AlphaNumericPasswordValidationRule();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testValidate(){
        passwordValidationRule.validate("test123");
    }

    @Test
    public void testValidate_chars(){
        passwordValidationRule.validate("test");
    }

    @Test
    public void testValidate_digits(){
        passwordValidationRule.validate("123");
    }

    @Test
    public void testValidate_specialChars(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage("password must be a combination of alphanumeric characters");

        passwordValidationRule.validate("test@$3");
    }
}
