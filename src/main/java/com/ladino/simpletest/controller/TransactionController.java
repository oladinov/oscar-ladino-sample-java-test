package com.ladino.simpletest.controller;

import com.ladino.simpletest.business.TransactionReportBusiness;
import com.ladino.simpletest.dao.TransactionDao;
import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionRequest;
import com.ladino.simpletest.model.TransactionSum;
import com.ladino.simpletest.model.TransactionWeekReport;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "")
public class TransactionController {

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    TransactionReportBusiness transactionReportBusiness;

    @PostMapping(value = "/user/{userId}/transaction/add")

    public Transaction addTransaction(@RequestBody TransactionRequest request, @PathVariable Integer userId) {
        Transaction transaction = Transaction.builder().amount(request.getAmount()).date(request.getDate()).description(request.getDescription()).build();
        Transaction response = transactionDao.addTransaction(transaction, userId);
        return response;
    }

    @GetMapping(value = "/user/{userId}/transaction/{transactionId}")
    public Transaction showTransaction(@PathVariable Integer userId, @PathVariable String transactionId) {
        Transaction response = transactionDao.findByIdAndUserId(transactionId, userId);
        return response;
    }

    @GetMapping(value = "/user/{userId}/transaction/list")
    public List<Transaction> listOfTransactions(@PathVariable Integer userId) {
        List<Transaction> response = transactionDao.findByUserId(userId);
        return response;
    }

    @GetMapping(value = "/user/{userId}/transaction/sum")
    public TransactionSum sumOfTransactions(@PathVariable Integer userId) {
        TransactionSum response = transactionReportBusiness.findSumOfTransactions(userId);
        return response;
    }

    @GetMapping(value = "/user/{userId}/transaction/report")
    public List<TransactionWeekReport> transactionsReport(@PathVariable Integer userId) {
        List<TransactionWeekReport> response = transactionReportBusiness.getReport(userId);

        return response;
    }
}
