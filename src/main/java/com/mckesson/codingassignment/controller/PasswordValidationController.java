package com.mckesson.codingassignment.controller;

import com.mckesson.codingassignment.service.PasswordValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles Rest requests related to password validation
 */
@RestController
public class PasswordValidationController {

    @Autowired
    private PasswordValidationService passwordValidationService;


    /**
     * This method validates password using the rules configured in system
     * @param password - password to be validated
     */
    @RequestMapping(value = "password/validate", method = RequestMethod.POST)
    public ResponseEntity validate(@RequestBody String password) {
        passwordValidationService.validate(password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
