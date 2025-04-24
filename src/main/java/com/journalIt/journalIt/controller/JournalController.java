package com.journalIt.journalIt.controller;

import com.journalIt.journalIt.entity.JournalEntity;
import com.journalIt.journalIt.entity.UserEntity;
import com.journalIt.journalIt.service.JournalService;
import com.journalIt.journalIt.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalController {

    //final Map<String, JournalEntity> journal = new HashMap<>();

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @GetMapping("user/{username}")
    public ResponseEntity<?> getAllJournalOfUser(@PathVariable String username) {
        try {
            UserEntity user = userService.findByUserName(username);

            if (user == null || user.getJournalEntries() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found or no journal entries available.");
            }
            return new ResponseEntity<>(user.getJournalEntries(), HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());
        }
    }


    @GetMapping("id/{id}")
    public ResponseEntity<?> getJournalById(@PathVariable ObjectId id) {
        Optional<JournalEntity> journal = journalService.getById(id);

        if (journal.isPresent()) {
            return ResponseEntity.ok(journal.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found with this ID");
        }
    }

    @PostMapping("/{username}")
    @Transactional
    public ResponseEntity<?> createJournal(@RequestBody JournalEntity body, @PathVariable String username){
        try{
            body.setDate(LocalDateTime.now());
            journalService.saveEntry(body, username);
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch( Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("{username}/{id}")
    @Transactional
    public ResponseEntity<?> updateJournal(
            @PathVariable String username,
            @PathVariable ObjectId id,
            @RequestBody JournalEntity body){
        try{
            JournalEntity journalOld = journalService.getById(id).orElse(null);
            if(journalOld != null){
                journalOld.setTitle(body.getTitle() != null && !body.getTitle().isEmpty() ? body.getTitle() : journalOld.getTitle());
                journalOld.setContent(body.getContent() != null && !body.getContent().isEmpty() ? body.getContent() : journalOld.getContent());
            }

            journalService.saveEntry(journalOld);
            return new ResponseEntity<>("Updated Successfully!", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{username}/{id}")
    public  ResponseEntity<?> deleteJournalById(@PathVariable ObjectId id, @PathVariable String username){
        try {
            journalService.deleteJournalById(id, username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
