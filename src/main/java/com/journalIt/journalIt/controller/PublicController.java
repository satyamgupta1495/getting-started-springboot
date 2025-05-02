package com.journalIt.journalIt.controller;

import com.journalIt.journalIt.entity.UserEntity;
import com.journalIt.journalIt.service.UserDetailsServiceImpl;
import com.journalIt.journalIt.service.UserService;
import com.journalIt.journalIt.utils.Jwtutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;  //? AuthenticationManager => spring security inbuilt function

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private Jwtutils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserEntity userDetails){
        try {
            userService.saveUserWithEncryptedPassword(userDetails);
            return new ResponseEntity<>("User created successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User cannot be created : " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserEntity user){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());  //? UserDetails => spring security inbuilt
            String token = jwtUtils.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authentication failed : " + e.getMessage());
        }
    }

}
