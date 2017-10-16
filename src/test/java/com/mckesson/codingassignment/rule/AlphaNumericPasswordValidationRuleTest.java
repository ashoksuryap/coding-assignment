package com.mckesson.codingassignment.rule;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlphaNumericPasswordValidationRuleTest {

    private static final String VALIDATION_MESSAGE = "password must consist of a mixture of lowercase letters and numerical digits only, with at least one of each";
    private PasswordValidationRule passwordValidationRule = new AlphaNumericPasswordValidationRule();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testValidate(){
        passwordValidationRule.validate("test1234");
    }

    @Test
    public void testValidate_upperCase(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage(VALIDATION_MESSAGE);

        passwordValidationRule.validate("TEST");
    }

    @Test
    public void testValidate_digits(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage(VALIDATION_MESSAGE);

        passwordValidationRule.validate("123");
    }

    @Test
    public void testValidate_chars(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage(VALIDATION_MESSAGE);

        passwordValidationRule.validate("test");
    }

    @Test
    public void testValidate_specialChars(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage(VALIDATION_MESSAGE);

        passwordValidationRule.validate("@@test@$3");
    }

    @Test
    public void testValidate_upperLowerCase(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage(VALIDATION_MESSAGE);

        passwordValidationRule.validate("teST");
    }

    @Test
    public void testValidate_upperLowerCase_Digits(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage(VALIDATION_MESSAGE);

        passwordValidationRule.validate("teST123");
    }

    @Test
    public void testValidate_upperCase_Digits(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage(VALIDATION_MESSAGE);

        passwordValidationRule.validate("TEST123");
    }
}
