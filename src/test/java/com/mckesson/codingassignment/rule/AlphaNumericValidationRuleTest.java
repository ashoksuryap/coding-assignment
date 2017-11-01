package com.mckesson.codingassignment.rule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AlphaNumericValidationRuleTest {

    private static final String VALIDATION_ERROR_MESSAGE = "password must consist of a mixture of lowercase letters and numerical digits only, with at least one of each";
    private static final String regex = "[\\p{Lower}]+[\\p{Digit}]+|[\\p{Digit}]+[\\p{Lower}]+";
    private static final String ALPHANUMERIC_VALIDATION = "alphanumeric.validation";

    @Mock
    private MessageSource messageSource;

    private ValidationRule validationRule;

    @Before
    public void setUp() throws Exception{
        validationRule = new AlphaNumericValidationRule(regex, messageSource);

        when(messageSource.getMessage(eq(ALPHANUMERIC_VALIDATION),any(Object[].class),anyObject())).thenReturn(VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate(){
        RuleResult ruleResult = validationRule.validate("test1234");
        Assert.assertEquals(ruleResult.isSuccess(), true);
        Assert.assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_upperCase(){
        RuleResult ruleResult = validationRule.validate("TEST");
        Assert.assertEquals(ruleResult.isSuccess(), false);
        Assert.assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_digits(){
        RuleResult ruleResult = validationRule.validate("123");
        Assert.assertEquals(ruleResult.isSuccess(), false);
        Assert.assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_chars(){
        RuleResult ruleResult = validationRule.validate("test");
        Assert.assertEquals(ruleResult.isSuccess(), false);
        Assert.assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_specialChars(){
        RuleResult ruleResult = validationRule.validate("test123$!@#%");
        Assert.assertEquals(ruleResult.isSuccess(), false);
        Assert.assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_upperLowerCase(){
        RuleResult ruleResult = validationRule.validate("teST");
        Assert.assertEquals(ruleResult.isSuccess(), false);
        Assert.assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_upperLowerCase_Digits(){
        RuleResult ruleResult = validationRule.validate("teST123");
        Assert.assertEquals(ruleResult.isSuccess(), false);
        Assert.assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_upperCase_Digits(){
        RuleResult ruleResult = validationRule.validate("TEST123");
        Assert.assertEquals(ruleResult.isSuccess(), false);
        Assert.assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }
}
