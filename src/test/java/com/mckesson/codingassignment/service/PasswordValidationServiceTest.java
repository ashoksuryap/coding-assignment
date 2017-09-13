package com.mckesson.codingassignment.service;


import com.mckesson.codingassignment.exception.PasswordValidationException;
import com.mckesson.codingassignment.rule.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PasswordValidationServiceTest {

    private static final int PASSWORD_MIN_LENGTH = 5;
    private static final int PASSWORD_MAX_LENGTH = 12;

    @Spy
    private PasswordValidationRule alphaNumericPasswordValidationRule = new AlphaNumericPasswordValidationRule();

    @Spy
    private PasswordValidationRule letterPasswordValidationRule = new LetterPasswordValidationRule();

    @Spy
    private PasswordValidationRule numericPasswordValidationRule = new NumericPasswordValidationRule();

    @Spy
    private PasswordValidationRule passwordLengthValidationRule = new PasswordLengthValidationRule();

    @Spy
    private PasswordValidationRule characterSequencePasswordValidationRule = new CharacterSequencePasswordValidationRule();

    private PasswordValidationService passwordValidationService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception{
        Map<String, PasswordValidationRule> passwordValidationRules = new HashMap<>();
        passwordValidationRules.put("ALPHA_NUMERIC",alphaNumericPasswordValidationRule );
        passwordValidationRules.put("LETTER",letterPasswordValidationRule );
        passwordValidationRules.put("NUMERIC",numericPasswordValidationRule );
        passwordValidationRules.put("LENGTH",passwordLengthValidationRule );
        passwordValidationRules.put("CHAR_SEQUENCE",characterSequencePasswordValidationRule );
        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules);

        Field passwordMinLengthField = ReflectionUtils.findField(PasswordLengthValidationRule.class, "passwordMinLength");
        ReflectionUtils.makeAccessible(passwordMinLengthField);
        ReflectionUtils.setField(passwordMinLengthField, passwordLengthValidationRule, PASSWORD_MIN_LENGTH);

        Field passwordMaxLengthField = ReflectionUtils.findField(PasswordLengthValidationRule.class, "passwordMaxLength");
        ReflectionUtils.makeAccessible(passwordMaxLengthField);
        ReflectionUtils.setField(passwordMaxLengthField, passwordLengthValidationRule, PASSWORD_MAX_LENGTH);
    }

    @Test
    public void testValidate(){
        passwordValidationService.validate("test1234");
        verify(alphaNumericPasswordValidationRule, times(1)).validate("test1234");
        verify(letterPasswordValidationRule, times(1)).validate("test1234");
        verify(numericPasswordValidationRule, times(1)).validate("test1234");
        verify(passwordLengthValidationRule, times(1)).validate("test1234");
        verify(characterSequencePasswordValidationRule, times(1)).validate("test1234");
    }

    @Test
    public void testValidate_null() {
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage("password cannot be empty or null");

        passwordValidationService.validate(null);
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                                letterPasswordValidationRule,
                                numericPasswordValidationRule,
                                passwordLengthValidationRule,
                                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_alphaNumeric() {
        List<String> configuredRules = Arrays.asList("ALPHA_NUMERIC");

        Field configuredRulesField = ReflectionUtils.findField(PasswordValidationServiceImpl.class, "configuredRules");
        ReflectionUtils.makeAccessible(configuredRulesField);
        ReflectionUtils.setField(configuredRulesField, passwordValidationService, configuredRules);

        passwordValidationService.validate("test1234");
        verify(alphaNumericPasswordValidationRule, times(1)).validate("test1234");
        verifyZeroInteractions(letterPasswordValidationRule,
                                numericPasswordValidationRule,
                                passwordLengthValidationRule,
                                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_letter() {
        List<String> configuredRules = Arrays.asList("LETTER");

        Field configuredRulesField = ReflectionUtils.findField(PasswordValidationServiceImpl.class, "configuredRules");
        ReflectionUtils.makeAccessible(configuredRulesField);
        ReflectionUtils.setField(configuredRulesField, passwordValidationService, configuredRules);

        passwordValidationService.validate("test1234");
        verify(letterPasswordValidationRule, times(1)).validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                numericPasswordValidationRule,
                passwordLengthValidationRule,
                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_numeric() {
        List<String> configuredRules = Arrays.asList("NUMERIC");

        Field configuredRulesField = ReflectionUtils.findField(PasswordValidationServiceImpl.class, "configuredRules");
        ReflectionUtils.makeAccessible(configuredRulesField);
        ReflectionUtils.setField(configuredRulesField, passwordValidationService, configuredRules);

        passwordValidationService.validate("test1234");
        verify(numericPasswordValidationRule , times(1)).validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                letterPasswordValidationRule,
                passwordLengthValidationRule,
                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_length() {
        List<String> configuredRules = Arrays.asList("LENGTH");

        Field configuredRulesField = ReflectionUtils.findField(PasswordValidationServiceImpl.class, "configuredRules");
        ReflectionUtils.makeAccessible(configuredRulesField);
        ReflectionUtils.setField(configuredRulesField, passwordValidationService, configuredRules);

        passwordValidationService.validate("test1234");
        verify(passwordLengthValidationRule  , times(1)).validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                letterPasswordValidationRule,
                numericPasswordValidationRule,
                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_сharSeq() {
        List<String> configuredRules = Arrays.asList("CHAR_SEQUENCE");

        Field configuredRulesField = ReflectionUtils.findField(PasswordValidationServiceImpl.class, "configuredRules");
        ReflectionUtils.makeAccessible(configuredRulesField);
        ReflectionUtils.setField(configuredRulesField, passwordValidationService, configuredRules);

        passwordValidationService.validate("test1234");
        verify(characterSequencePasswordValidationRule, times(1)).validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                letterPasswordValidationRule,
                numericPasswordValidationRule,
                passwordLengthValidationRule);
    }

    @Test
    public void testValidate_numericAndLength() {
        List<String> configuredRules = Arrays.asList("LENGTH","NUMERIC");

        Field configuredRulesField = ReflectionUtils.findField(PasswordValidationServiceImpl.class, "configuredRules");
        ReflectionUtils.makeAccessible(configuredRulesField);
        ReflectionUtils.setField(configuredRulesField, passwordValidationService, configuredRules);

        passwordValidationService.validate("test1234");
        verify(passwordLengthValidationRule  , times(1)).validate("test1234");
        verify(numericPasswordValidationRule  , times(1)).validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                letterPasswordValidationRule,
                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_invalidRuleName() {
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage("configured password validation rule INVALID is not available in system.");

        List<String> configuredRules = Arrays.asList("INVALID");

        Field configuredRulesField = ReflectionUtils.findField(PasswordValidationServiceImpl.class, "configuredRules");
        ReflectionUtils.makeAccessible(configuredRulesField);
        ReflectionUtils.setField(configuredRulesField, passwordValidationService, configuredRules);

        passwordValidationService.validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                passwordLengthValidationRule,
                numericPasswordValidationRule,
                letterPasswordValidationRule,
                characterSequencePasswordValidationRule);
    }
}

