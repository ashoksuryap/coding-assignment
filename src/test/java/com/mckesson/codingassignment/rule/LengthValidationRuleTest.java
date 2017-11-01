package com.mckesson.codingassignment.rule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LengthValidationRuleTest {

    private static final String VALIDATION_ERROR_MESSAGE = "password length must be between 5 and 12 characters";
    private static final String LENGTH_VALIDATION = "length.validation";

    private ValidationRule validationRule;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception{
        int PASSWORD_MIN_LENGTH = 5;
        int PASSWORD_MAX_LENGTH = 12;
        validationRule = new LengthValidationRule(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH, messageSource);

        when(messageSource.getMessage(eq(LENGTH_VALIDATION),any(Object[].class),anyObject())).thenReturn(VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate(){
        RuleResult ruleResult = validationRule.validate("test1234");
        Assert.assertEquals(ruleResult.isSuccess(), true);
        Assert.assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_5chars(){
        RuleResult ruleResult = validationRule.validate("test5");
        Assert.assertEquals(ruleResult.isSuccess(), true);
        Assert.assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_lessThan5chars(){
        RuleResult ruleResult = validationRule.validate("testtest123");
        Assert.assertEquals(ruleResult.isSuccess(), true);
        Assert.assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_12chars(){
        RuleResult ruleResult = validationRule.validate("test12345678");
        Assert.assertEquals(ruleResult.isSuccess(), true);
        Assert.assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_greaterThan12chars(){
        RuleResult ruleResult = validationRule.validate("testing123456");
        Assert.assertEquals(ruleResult.isSuccess(), false);
        Assert.assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }
}
