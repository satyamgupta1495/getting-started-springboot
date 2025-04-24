package com.journalIt.journalIt.controller;

import com.journalIt.journalIt.entity.UserEntity;
import com.journalIt.journalIt.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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

    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody UserEntity userDetails){
        try {
            userDetails.setDate(LocalDateTime.now());
            userService.saveUser(userDetails);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User cannot be created : " + e.getMessage());
        }

    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUserDetails(@RequestBody UserEntity user, @PathVariable String username){
        try {
            UserEntity userInDB = userService.findByUserName(username);
            if(userInDB != null){
                userInDB.setUserName(user.getUserName());
                userInDB.setPassword(user.getPassword());
                userService.saveUser(userInDB);
            }
            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user details!");
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable ObjectId id){
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
