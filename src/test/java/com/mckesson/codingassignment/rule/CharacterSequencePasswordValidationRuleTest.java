package com.mckesson.codingassignment.rule;


import com.mckesson.codingassignment.exception.PasswordValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CharacterSequencePasswordValidationRuleTest {

    private PasswordValidationRule passwordValidationRule = new CharacterSequencePasswordValidationRule();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testValidate(){
        passwordValidationRule.validate("test1234");
    }

    @Test
    public void testValidate_ImmediateCharSeq(){
        expectedEx.expect(PasswordValidationException.class);
        expectedEx.expectMessage("password must not contain any sequence of characters immediately followed by the same sequence");

        passwordValidationRule.validate("testtest123");
    }

    @Test
    public void testValidate_NotImmediateCharSeq(){
        passwordValidationRule.validate("test123test");
    }
}
