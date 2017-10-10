package com.mckesson.codingassignment.service;


import com.mckesson.codingassignment.exception.PasswordValidationException;
import com.mckesson.codingassignment.rule.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

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
    private PasswordValidationRule passwordLengthValidationRule = new PasswordLengthValidationRule(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH);

    @Spy
    private PasswordValidationRule characterSequencePasswordValidationRule = new CharacterSequencePasswordValidationRule();

    private PasswordValidationService passwordValidationService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testValidate(){
        Map<String, PasswordValidationRule> passwordValidationRules = getPasswordValidationRules();
        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules, Collections.emptyList());

        passwordValidationService.validate("test1234");
        verify(alphaNumericPasswordValidationRule, times(1)).validate("test1234");
        verify(letterPasswordValidationRule, times(1)).validate("test1234");
        verify(numericPasswordValidationRule, times(1)).validate("test1234");
        verify(passwordLengthValidationRule, times(1)).validate("test1234");
        verify(characterSequencePasswordValidationRule, times(1)).validate("test1234");
    }

    @Test
    public void testValidate_null() {
        Map<String, PasswordValidationRule> passwordValidationRules = getPasswordValidationRules();
        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules, Collections.emptyList());

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
        Map<String, PasswordValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("ALPHA_NUMERIC");

        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules, configuredRules);

        passwordValidationService.validate("test1234");
        verify(alphaNumericPasswordValidationRule, times(1)).validate("test1234");
        verifyZeroInteractions(letterPasswordValidationRule,
                                numericPasswordValidationRule,
                                passwordLengthValidationRule,
                                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_letter() {
        Map<String, PasswordValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("LETTER");

        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules, configuredRules);

        passwordValidationService.validate("test1234");
        verify(letterPasswordValidationRule, times(1)).validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                numericPasswordValidationRule,
                passwordLengthValidationRule,
                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_numeric() {
        Map<String, PasswordValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("NUMERIC");

        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules, configuredRules);

        passwordValidationService.validate("test1234");
        verify(numericPasswordValidationRule , times(1)).validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                letterPasswordValidationRule,
                passwordLengthValidationRule,
                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_length() {
        Map<String, PasswordValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("LENGTH");

        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules, configuredRules);

        passwordValidationService.validate("test1234");
        verify(passwordLengthValidationRule  , times(1)).validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                letterPasswordValidationRule,
                numericPasswordValidationRule,
                characterSequencePasswordValidationRule);
    }

    @Test
    public void testValidate_сharSeq() {
        Map<String, PasswordValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("CHAR_SEQUENCE");

        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules, configuredRules);

        passwordValidationService.validate("test1234");
        verify(characterSequencePasswordValidationRule, times(1)).validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                letterPasswordValidationRule,
                numericPasswordValidationRule,
                passwordLengthValidationRule);
    }

    @Test
    public void testValidate_numericAndLength() {
        Map<String, PasswordValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("LENGTH","NUMERIC");

        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules, configuredRules);

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

        Map<String, PasswordValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("INVALID");

        passwordValidationService = new PasswordValidationServiceImpl(passwordValidationRules, configuredRules);

        passwordValidationService.validate("test1234");
        verifyZeroInteractions(alphaNumericPasswordValidationRule,
                passwordLengthValidationRule,
                numericPasswordValidationRule,
                letterPasswordValidationRule,
                characterSequencePasswordValidationRule);
    }

    private Map<String, PasswordValidationRule> getPasswordValidationRules() {
        Map<String, PasswordValidationRule> passwordValidationRules = new HashMap<>();
        passwordValidationRules.put("ALPHA_NUMERIC",alphaNumericPasswordValidationRule );
        passwordValidationRules.put("LETTER",letterPasswordValidationRule );
        passwordValidationRules.put("NUMERIC",numericPasswordValidationRule );
        passwordValidationRules.put("LENGTH",passwordLengthValidationRule );
        passwordValidationRules.put("CHAR_SEQUENCE",characterSequencePasswordValidationRule );
        return passwordValidationRules;
    }
}

