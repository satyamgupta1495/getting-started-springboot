package com.journalIt.journalIt.entity;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class UserEntity {

    @Id
    private ObjectId userID;

    @NonNull
    @Indexed(unique = true)
    private String userName;

    @NonNull
    private String password;

    @DBRef
    private List<JournalEntity> journalEntries = new ArrayList<>();

}
