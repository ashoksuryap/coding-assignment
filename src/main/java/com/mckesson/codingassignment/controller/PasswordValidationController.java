package com.mckesson.codingassignment.controller;

import com.mckesson.codingassignment.service.PasswordValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles Rest requests related to password validation
 */
@RestController
@RequestMapping(path = "password")
public class PasswordValidationController {

    @Autowired
    private PasswordValidationService passwordValidationService;


    /**
     * This method validates password using the rules configured in system
     * @param password - password to be validated
     */
    @PostMapping(value = "/validate")
    public ResponseEntity validate(@RequestBody String password) {
        passwordValidationService.validate(password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
