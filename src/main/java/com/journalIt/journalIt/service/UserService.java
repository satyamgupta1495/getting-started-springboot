package com.journalIt.journalIt.service;

import com.journalIt.journalIt.entity.JournalEntity;
import com.journalIt.journalIt.entity.UserEntity;
import com.journalIt.journalIt.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequestMapping("/user")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public List<UserEntity> getAllUser(){
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(ObjectId id){
        return userRepository.findById(id);
    }

    public void saveUser(UserEntity userDetails){
        userRepository.save(userDetails);
    }

    public void saveUserWithEncryptedPassword(UserEntity userDetails){
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        userDetails.setRoles(Arrays.asList("ADMIN", "USER"));
        userRepository.save(userDetails);
    }

    public UserEntity findByUserName(String user){
        return userRepository.findByUserName(user);
    }

    public void deleteUser(ObjectId id){
        userRepository.deleteById(id);
    }

}
