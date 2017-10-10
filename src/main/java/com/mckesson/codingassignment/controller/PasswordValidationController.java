package com.mckesson.codingassignment.controller;

import com.mckesson.codingassignment.service.PasswordValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles Rest requests related to password validation
 */
@RestController
@RequestMapping(path = "password")
public class PasswordValidationController {

    private PasswordValidationService passwordValidationService;

    @Autowired
    public PasswordValidationController(PasswordValidationService passwordValidationService) {
        this.passwordValidationService = passwordValidationService;
    }

    /**
     * This method validates password using the rules configured in system
     * @param password - password to be validated
     * @return  represents the entire HTTP response for validation request
     *          Status Code 200 - Ok. Password validation was successful.
     *          Status Code 400 - Bad Request. Password validation failed.
     */
    @PostMapping(value = "/validate")
    public ResponseEntity validate(@RequestBody String password) {
        passwordValidationService.validate(password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
