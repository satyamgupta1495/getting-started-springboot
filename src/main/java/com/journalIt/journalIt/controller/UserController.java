package com.journalIt.journalIt.controller;

import com.journalIt.journalIt.entity.UserEntity;
import com.journalIt.journalIt.repository.UserRepository;
import com.journalIt.journalIt.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        try {
            return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users list is empty" + e.getMessage());
        }

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable ObjectId id){
        try {
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with this ID : " + e.getMessage());
        }

    }

    @PutMapping
    public ResponseEntity<?> updateUserDetails(@RequestBody UserEntity user){
        try {
            Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            UserEntity userInDB = userService.findByUserName(userName);
            userInDB.setUserName(user.getUserName());
            userInDB.setPassword(user.getPassword());
            userService.saveUserWithEncryptedPassword(userInDB);

            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user details!");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userRepository.deleteByUserName(authentication.getName());
            return new ResponseEntity<>("Deleted Successfully!", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
