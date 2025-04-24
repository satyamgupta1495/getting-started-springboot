package com.journalIt.journalIt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JournalItApplication {

	public static void main(String[] args) {
		SpringApplication.run(JournalItApplication.class, args);
	}


	@Bean
	public PlatformTransactionManager trxnManager(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}

}

//MongoTransactionManager implements PlatformTransactionManager [does the work od commiting and rollback]

// NOTE:
// 		 1- On higher level Session = Transactional context;
//  	 2- dbFactory helps us to create connection with mongoDB
// 		 3- Without the implementation of this [PlatformTransactionManager] who's going to handle the transaction
// 		 4- BEAN ?? "Hey Spring, when you build the application context, call this method and manage the returned object (MongoTransactionManager) as a bean."
