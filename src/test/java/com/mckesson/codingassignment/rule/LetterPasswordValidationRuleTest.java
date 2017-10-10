package com.mckesson.codingassignment.rule;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LetterPasswordValidationRuleTest {

    private PasswordValidationRule passwordValidationRule = new LetterPasswordValidationRule();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testValidate(){
        passwordValidationRule.validate("test1234");
    }

    @Test
    public void testValidate_upperCase(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage("password must contain at least one letter and all letters should be in lower case");

        passwordValidationRule.validate("teSt");
    }

    @Test
    public void testValidate_digits(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage("password must contain at least one letter and all letters should be in lower case");

        passwordValidationRule.validate("123");
    }

    @Test
    public void testValidate_specialChars(){
        passwordValidationRule.validate("@@test@$3");
    }
}
