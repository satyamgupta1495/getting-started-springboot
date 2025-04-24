package com.journalIt.journalIt.service;

import com.journalIt.journalIt.entity.JournalEntity;
import com.journalIt.journalIt.entity.UserEntity;
import com.journalIt.journalIt.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalService {


    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntity> getAll(){
        return journalRepository.findAll();
    }

    public Optional<JournalEntity> getById(ObjectId id){
        return journalRepository.findById(id);
    }

    public void saveEntry(JournalEntity journal, String username){
        UserEntity user = userService.findByUserName(username);
        journal.setDate(LocalDateTime.now());
        JournalEntity savedJournal = journalRepository.save(journal);
        user.getJournalEntries().add(savedJournal);
        userService.saveUser(user);
    }

    public void saveEntry(JournalEntity journal){
        journalRepository.save(journal);
    }

    public void deleteJournalById(ObjectId id, String username){
        UserEntity user = userService.findByUserName(username);
        user.getJournalEntries().removeIf(journal -> journal.getId().equals(id));
        userService.saveUser(user);
        journalRepository.deleteById(id);
    }

}
