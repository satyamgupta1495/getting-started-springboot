package com.journalIt.journalIt.controller;

import com.journalIt.journalIt.entity.JournalEntity;
import com.journalIt.journalIt.entity.UserEntity;
import com.journalIt.journalIt.service.JournalService;
import com.journalIt.journalIt.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalController {

    //final Map<String, JournalEntity> journal = new HashMap<>();

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalOfUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserEntity user = userService.findByUserName(authentication.getName());

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByUserName(authentication.getName());
        List<JournalEntity> allJournal = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();
        if(!allJournal.isEmpty()){
            Optional<JournalEntity> journal = journalService.getById(id);
            if (journal.isPresent()) {
                return ResponseEntity.ok(journal.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found with this ID");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found with this ID");
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createJournal(@RequestBody JournalEntity body){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            body.setDate(LocalDateTime.now());
            journalService.saveEntry(body, authentication.getName());
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch( Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("id/{id}")
    @Transactional
    public ResponseEntity<?> updateJournal(
            @PathVariable ObjectId id,
            @RequestBody JournalEntity body){
        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity user = userService.findByUserName(authentication.getName());
            List<JournalEntity> allJournal = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();

            if(!allJournal.isEmpty()){
               Optional<JournalEntity> journalEntry =  journalService.getById(id);
                JournalEntity journalOld = journalService.getById(id).orElse(null);
                if(journalEntry.isPresent() && journalOld != null){
                    journalOld.setTitle(body.getTitle() != null && !body.getTitle().isEmpty() ? body.getTitle() : journalOld.getTitle());
                    journalOld.setContent(body.getContent() != null && !body.getContent().isEmpty() ? body.getContent() : journalOld.getContent());
                }
                journalService.saveEntry(journalOld);
            }
            return new ResponseEntity<>("Updated Successfully!", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("id/{id}")
    public  ResponseEntity<?> deleteJournalById(@PathVariable ObjectId id){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            boolean removed = journalService.deleteJournalById(id, username);
            if (removed) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
