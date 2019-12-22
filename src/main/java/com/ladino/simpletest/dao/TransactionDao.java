package com.ladino.simpletest.dao;

import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionSum;

import java.util.List;
import java.util.Map;

public interface TransactionDao {

    public Transaction addTransaction(Transaction transaction, Integer userId);

    Transaction findByIdAndUserId(String id, Integer userId);

    List<Transaction> findByUserId(Integer userId);

    TransactionSum findSumOfTransactions(Integer userId);
}
