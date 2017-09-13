package com.mckesson.codingassignment.rule;

import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class PasswordLengthValidationRuleTest {

    private static final int PASSWORD_MIN_LENGTH = 5;
    private static final int PASSWORD_MAX_LENGTH = 12;
    private PasswordValidationRule passwordValidationRule;

    @Before
    public void setUp() throws Exception{
        passwordValidationRule = new PasswordLengthValidationRule();

        Field passwordMinLengthField = ReflectionUtils.findField(PasswordLengthValidationRule.class, "passwordMinLength");
        ReflectionUtils.makeAccessible(passwordMinLengthField);
        ReflectionUtils.setField(passwordMinLengthField, passwordValidationRule, PASSWORD_MIN_LENGTH);

        Field passwordMaxLengthField = ReflectionUtils.findField(PasswordLengthValidationRule.class, "passwordMaxLength");
        ReflectionUtils.makeAccessible(passwordMaxLengthField);
        ReflectionUtils.setField(passwordMaxLengthField, passwordValidationRule, PASSWORD_MAX_LENGTH);
    }

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
