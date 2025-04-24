package com.journalIt.journalIt.controller;

import com.journalIt.journalIt.entity.JournalEntity;
import com.journalIt.journalIt.service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalController {

    //final Map<String, JournalEntity> journal = new HashMap<>();

    @Autowired
    private JournalService journalService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(journalService.getAll(), HttpStatus.OK);
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

    @PostMapping
    public ResponseEntity<?> createJournal(@RequestBody JournalEntity body){
        try{
            body.setDate(LocalDateTime.now());
            journalService.saveEntry(body);
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch( Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournal(@PathVariable ObjectId id, @RequestBody JournalEntity body){
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

    @DeleteMapping("id/{id}")
    public  ResponseEntity<?> deleteJournalById(@PathVariable ObjectId id){
        try {
            journalService.deleteJournalById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
