package com.mckesson.codingassignment.service;


import com.mckesson.codingassignment.exception.RuleConfigurationException;
import com.mckesson.codingassignment.rule.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceTest {

    private static final int PASSWORD_MIN_LENGTH = 5;
    private static final int PASSWORD_MAX_LENGTH = 12;

    private static final String alphaNumericRegex = "[\\p{Lower}]+[\\p{Digit}]+|[\\p{Digit}]+[\\p{Lower}]+";
    private static final String charSequenceRegex = "(\\p{Alnum}{2,})\\1";

    @Mock
    private MessageSource messageSource;

    private ValidationRule alphaNumericValidationRule;

    private ValidationRule passwordLengthValidationRule;

    private ValidationRule characterSequenceValidationRule;

    private PasswordService passwordService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception{
        alphaNumericValidationRule = spy(new AlphaNumericValidationRule(alphaNumericRegex, messageSource));
        passwordLengthValidationRule = spy(new LengthValidationRule(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH, messageSource));
        characterSequenceValidationRule = spy(new CharacterSequenceValiationRule(charSequenceRegex, messageSource));

        when(messageSource.getMessage(anyString(),any(Object[].class),anyObject())).thenReturn("Test Message");
    }

    @Test
    public void testValidate() {
        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        passwordService = new PasswordServiceImpl(passwordValidationRules, Collections.emptyList());

        List<RuleResult> ruleResults = passwordService.validate("test1234");
        verify(alphaNumericValidationRule, times(1)).validate("test1234");
        verify(passwordLengthValidationRule, times(1)).validate("test1234");
        verify(characterSequenceValidationRule, times(1)).validate("test1234");
        ruleResults.forEach(ruleResult -> {
                    Assert.assertEquals(ruleResult.isSuccess(), true);
                    Assert.assertEquals(ruleResult.getErrorMessage(), null);
                }
        );
    }

    @Test
    public void testValidate_null() {
        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        passwordService = new PasswordServiceImpl(passwordValidationRules, Collections.emptyList());

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("password cannot be empty or null");

        passwordService.validate(null);
        verifyZeroInteractions(alphaNumericValidationRule,
                passwordLengthValidationRule,
                characterSequenceValidationRule);
    }

    @Test
    public void testValidate_multipleRules_oneRuleFailed() {
        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("LENGTH", "ALPHA_NUMERIC");

        passwordService = new PasswordServiceImpl(passwordValidationRules, configuredRules);

        List<RuleResult> ruleResults = passwordService.validate("testing");
        verify(passwordLengthValidationRule, times(1)).validate("testing");
        verify(alphaNumericValidationRule, times(1)).validate("testing");
        verifyZeroInteractions(characterSequenceValidationRule);
        ruleResults.forEach(ruleResult -> {
                    if ("ALPHA_NUMERIC".equals(ruleResult.getName())) {
                        Assert.assertEquals(ruleResult.isSuccess(), false);
                        Assert.assertNotEquals(ruleResult.getErrorMessage(), null);
                    } else {
                        Assert.assertEquals(ruleResult.isSuccess(), true);
                        Assert.assertEquals(ruleResult.getErrorMessage(), null);
                    }
                }
        );
    }

    @Test
    public void testValidate_multipleRules_allRulesFailed() {
        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("CHAR_SEQUENCE", "ALPHA_NUMERIC");

        passwordService = new PasswordServiceImpl(passwordValidationRules, configuredRules);

        List<RuleResult> ruleResults = passwordService.validate("testtest");
        verify(characterSequenceValidationRule, times(1)).validate("testtest");
        verify(alphaNumericValidationRule, times(1)).validate("testtest");
        verifyZeroInteractions(passwordLengthValidationRule);
        ruleResults.forEach(ruleResult -> {
                    Assert.assertEquals(ruleResult.isSuccess(), false);
                    Assert.assertNotEquals(ruleResult.getErrorMessage(), null);
                }
        );
    }

    @Test
    public void testValidate_invalidRuleName() {
        expectedEx.expect(RuleConfigurationException.class);
        expectedEx.expectMessage("configured password validation rule INVALID is not available in system.");

        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("INVALID");

        passwordService = new PasswordServiceImpl(passwordValidationRules, configuredRules);

        passwordService.validate("test1234");
        verifyZeroInteractions(passwordLengthValidationRule,
                alphaNumericValidationRule,
                characterSequenceValidationRule);
    }

    private Map<String, ValidationRule> getPasswordValidationRules() {
        Map<String, ValidationRule> passwordValidationRules = new HashMap<>();
        passwordValidationRules.put("ALPHA_NUMERIC", alphaNumericValidationRule);
        passwordValidationRules.put("LENGTH", passwordLengthValidationRule);
        passwordValidationRules.put("CHAR_SEQUENCE", characterSequenceValidationRule);
        return passwordValidationRules;
    }
}

