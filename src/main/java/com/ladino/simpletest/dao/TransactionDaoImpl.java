package com.ladino.simpletest.dao;

import com.ladino.simpletest.exception.TransactionNotFoundException;
import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionSum;
import com.ladino.simpletest.repository.TransactionRepository;
import com.ladino.simpletest.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionDaoImpl implements TransactionDao {

    Logger logger = LoggerFactory.getLogger(TransactionDaoImpl.class);

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction addTransaction(Transaction transaction, Integer userId) {
        String uuid = Utils.generateUUID();
        //Logged this info since this is not a stateless method
        logger.info("Persistence of transaction with ID {} for userID {}", uuid, userId);

        transaction.setTransactionId(uuid);
        transaction.setUserId(userId);

        transactionRepository.save(transaction);
        return transaction;
    }

    @Override
    public Transaction findByIdAndUserId(String id, Integer userId) {
        Optional<Transaction> transaction = transactionRepository.findByTransactionIdAndUserId(id, userId);

        return transaction.orElseThrow(TransactionNotFoundException::new);
    }

    @Override
    public List<Transaction> findByUserId(Integer userId) {
        List<Transaction> findByUserId = transactionRepository.findByUserId(userId);
        return findByUserId;
    }

    @Override
    public TransactionSum findSumOfTransactions(Integer userId) {
        TransactionSum sum = transactionRepository.sumTransactionsByUser(userId);
        return sum;
    }
}
