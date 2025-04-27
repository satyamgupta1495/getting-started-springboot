package com.journalIt.journalIt.controller;

import com.journalIt.journalIt.entity.UserEntity;
import com.journalIt.journalIt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> createNewUser(@RequestBody UserEntity userDetails){
        try {
            userService.saveUserWithEncryptedPassword(userDetails);
            return new ResponseEntity<>("User created successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User cannot be created : " + e.getMessage());
        }

    }

}
