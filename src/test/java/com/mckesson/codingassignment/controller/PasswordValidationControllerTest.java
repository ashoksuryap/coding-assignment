package com.mckesson.codingassignment.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PasswordValidationControllerTest {
    private static final String ALPHANUMERIC_VALIDATION_MESSAGE = "password must consist of a mixture of lowercase letters and numerical digits only, with at least one of each";
    private static final String LENGTH_VALIDATION_MESSAGE = "password length must be between 5 and 12 characters";
    private static final String CHARACTER_SEQUENCE_VALIDATION_MESSAGE = "password must not contain any sequence of characters immediately followed by the same sequence";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testValidate() {
        String password = "test123";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testValidate_null() {
        String password = null;
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("request body can not be null", responseEntity.getBody());
    }

    @Test
    public void testValidate_onlyLetters() {
        String password = "test";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(ALPHANUMERIC_VALIDATION_MESSAGE, responseEntity.getBody());
    }

    @Test
    public void testValidate_onlyNumbers() {
        String password = "123";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(ALPHANUMERIC_VALIDATION_MESSAGE, responseEntity.getBody());
    }

    @Test
    public void testValidate_upperCase() {
        String password = "teSt";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(ALPHANUMERIC_VALIDATION_MESSAGE, responseEntity.getBody());
    }

    @Test
    public void testValidate_specialSymbols() {
        String password = "test123$!@#%";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(ALPHANUMERIC_VALIDATION_MESSAGE, responseEntity.getBody());
    }

    @Test
    public void testValidate_length() {
        String password = "tes12345";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testValidate_5chars() {
        String password = "test8";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testValidate_lessThan5Chars() {
        String password = "tes1";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(LENGTH_VALIDATION_MESSAGE, responseEntity.getBody());
    }

    @Test
    public void testValidate_12chars() {
        String password = "test12345678";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testValidate_CharSeq() {
        String password = "testtest123";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(CHARACTER_SEQUENCE_VALIDATION_MESSAGE, responseEntity.getBody());
    }

    @Test
    public void testValidate_greaterThan12Chars() {
        String password = "test1234567891";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/password/validate", password, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(LENGTH_VALIDATION_MESSAGE, responseEntity.getBody());
    }
}
