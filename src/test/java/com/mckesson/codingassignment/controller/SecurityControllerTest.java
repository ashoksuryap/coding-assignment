package com.mckesson.codingassignment.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityControllerTest {

    private static final String ALPHA_NUMERIC = "ALPHA_NUMERIC";
    private static final String CHAR_SEQUENCE = "CHAR_SEQUENCE";
    private static final String LENGTH = "LENGTH";
    private static final String SECURITY_PASSWORD_URI = "/security/password";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testValidate() {
        String password = "test123";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(SECURITY_PASSWORD_URI, password, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testValidate_null() {
        String password = null;
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(SECURITY_PASSWORD_URI, password, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("request body can not be null", responseEntity.getBody());
    }

    @Test
    public void Should_FailAlphaNumericRule_ForLetters() throws JSONException {
        String password = "testing";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, false);
            put(CHAR_SEQUENCE, true);
            put(LENGTH, true);
        }});
    }

    @Test
    public void Should_FailAlphaNumericRule_ForNumbers() throws JSONException {
        String password = "123456";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, false);
            put(CHAR_SEQUENCE, true);
            put(LENGTH, true);
        }});
    }

    @Test
    public void Should_FailAlphaNumericRule_ForUppercaseLetters() throws JSONException {
        String password = "teStInG";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, false);
            put(CHAR_SEQUENCE, true);
            put(LENGTH, true);
        }});
    }

    @Test
    public void Should_FailAlphaNumericRule_ForSpecialSymbols() throws JSONException {
        String password = "test123$!@#%";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, false);
            put(CHAR_SEQUENCE, true);
            put(LENGTH, true);
        }});
    }

    @Test
    public void Should_FailLengthRule_ForInvalidLength() throws JSONException {
        String password = "tes1";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, true);
            put(CHAR_SEQUENCE, true);
            put(LENGTH, false);
        }});
    }

    @Test
    public void Should_PassLengthRule_ForMinimumLength() throws JSONException {
        String password = "test8";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, true);
            put(CHAR_SEQUENCE, true);
            put(LENGTH, true);
        }});
    }

    @Test
    public void Should_FailLengthRule_ForLessThanMinimumLength() throws JSONException {
        String password = "tes1";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, true);
            put(CHAR_SEQUENCE, true);
            put(LENGTH, false);
        }});
    }

    @Test
    public void Should_PassLengthRule_ForMaxLength() throws JSONException {
        String password = "test12345678";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, true);
            put(CHAR_SEQUENCE, true);
            put(LENGTH, true);
        }});
    }

    @Test
    public void Should_FailCharSequenceRule_ForImmediateSameSequence() throws JSONException {
        String password = "testtest123";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, true);
            put(CHAR_SEQUENCE, false);
            put(LENGTH, true);
        }});
    }

    @Test
    public void Should_FailLengthRule_ForGreaterThanMinimumLength() throws JSONException {
        String password = "test1234567891";

        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
            put(ALPHA_NUMERIC, true);
            put(CHAR_SEQUENCE, true);
            put(LENGTH, false);
        }});
    }

    private void verfiyRuleResult(String password, HashMap<String, Boolean> expectedResult) throws JSONException {
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(SECURITY_PASSWORD_URI, password, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JSONArray ruleArray = new JSONArray(responseEntity.getBody());
        for (int i = 0; i < ruleArray.length(); ++i) {
            JSONObject rule = ruleArray.getJSONObject(i);
            String ruleName = rule.getString("name");
            boolean ruleResult = rule.getBoolean("success");
            if (ALPHA_NUMERIC.equals(ruleName)) {
                assertEquals(expectedResult.get(ALPHA_NUMERIC), ruleResult);
            } else if (CHAR_SEQUENCE.equals(ruleName)) {
                assertEquals(expectedResult.get(CHAR_SEQUENCE), ruleResult);
            } else if (LENGTH.equals(ruleName)) {
                assertEquals(expectedResult.get(LENGTH), ruleResult);
            }
        }
    }
}
