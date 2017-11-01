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
public class CharacterSequenceValiationRuleTest {

    private static final String VALIDATION_ERROR_MESSAGE = "password must not contain any sequence of characters immediately followed by the same sequence";
    private static final String regex = "(\\p{Alnum}{2,})\\1";
    private static final String CHAR_SEQ_VALIDATION = "charactersequence.validation";

    private ValidationRule validationRule;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception{
        validationRule = new CharacterSequenceValiationRule(regex, messageSource);

        when(messageSource.getMessage(eq(CHAR_SEQ_VALIDATION),any(Object[].class),anyObject())).thenReturn(VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate(){
        RuleResult ruleResult = validationRule.validate("test1234");
        Assert.assertEquals(ruleResult.isSuccess(), true);
        Assert.assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_ImmediateCharSeq(){
        RuleResult ruleResult = validationRule.validate("testtest123");
        Assert.assertEquals(ruleResult.isSuccess(), false);
        Assert.assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_NotImmediateCharSeq(){
        RuleResult ruleResult = validationRule.validate("test123test");
        Assert.assertEquals(ruleResult.isSuccess(), true);
        Assert.assertEquals(ruleResult.getErrorMessage(), null);
    }
}
