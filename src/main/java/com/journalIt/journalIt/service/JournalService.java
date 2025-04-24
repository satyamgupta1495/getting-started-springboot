package com.journalIt.journalIt.service;

import com.journalIt.journalIt.entity.JournalEntity;
import com.journalIt.journalIt.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalService {


    @Autowired
    private JournalRepository journalRepository;


    public List<JournalEntity> getAll(){
        return journalRepository.findAll();
    }

    public Optional<JournalEntity> getById(ObjectId id){
        return journalRepository.findById(id);
    }

    public void saveEntry(JournalEntity journal){
        journalRepository.save(journal);
    }

    public void deleteJournalById(ObjectId id){
        journalRepository.deleteById(id);
    }

}
