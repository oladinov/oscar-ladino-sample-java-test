package com.ladino.simpletest.business;

import com.ladino.simpletest.dao.TransactionDao;
import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionSum;
import com.ladino.simpletest.model.TransactionWeekReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionReportBusiness {

    @Autowired
    TransactionDao transactionDao;

    public List<TransactionWeekReport> getReport(Integer userId) {
        return null;
    }

    public TransactionSum findSumOfTransactions(Integer userId) {
        List<Transaction> transactionList = transactionDao.findByUserId(userId);

        Double sum = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        TransactionSum transactionSum = TransactionSum.builder().total(sum).userId(userId).build();

        return transactionSum;
    }
}
