package com.journalIt.journalIt.controller;

import com.journalIt.journalIt.entity.UserEntity;
import com.journalIt.journalIt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-user")
    public ResponseEntity<?> getAllUsers(){
        try{
            List<UserEntity> users = userService.getAllUser();

            if(users.isEmpty()){
                return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody UserEntity adminDetails){
        try {
            UserEntity createdAdmin =  userService.saveAdminWithEncryptedPassword(adminDetails);
            return new ResponseEntity<>(createdAdmin, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while creating admin : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
