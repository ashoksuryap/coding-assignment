package com.mckesson.codingassignment.controller;

import com.mckesson.codingassignment.rule.RuleResult;
import com.mckesson.codingassignment.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handles Rest requests related to security
 */
@RestController
@RequestMapping(path = "security")
public class SecurityController {

    private PasswordService passwordService;

    @Autowired
    public SecurityController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    /**
     * This method validates password using the rules configured in system
     * @param password - password to be validated
     * @return  represents the entire HTTP response contains each rule result
     *          Status Code 200 - Ok.
     */
    @PostMapping(value = "/password")
    public ResponseEntity validate(@RequestBody String password) {
        List<RuleResult> ruleResults = passwordService.validate(password);
        return new ResponseEntity<List<RuleResult>>(ruleResults, HttpStatus.OK);
    }
}
