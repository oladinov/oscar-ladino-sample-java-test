package com.ladino.simpletest.repository;

import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionSum;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByUserId(Integer userId);

    Optional<Transaction> findByTransactionIdAndUserId(String id, Integer userId);

    /*
    I made this method to perform the sum. But then read again the description of the assessment and re made it
     in the business class.

    Left this method to show my first approach to it, which is still a valid way, just not what this exercise requires.
     */
    @Aggregation(pipeline = { "{ $match : { userId : ?0 } } " , "{ $group: { \"_id\" : ?0, total: { $sum: \"$amount\"} } } " } )
    TransactionSum sumTransactionsByUser(Integer userId);
}
