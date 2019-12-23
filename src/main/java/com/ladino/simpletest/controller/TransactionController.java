package com.ladino.simpletest.controller;

import com.ladino.simpletest.business.TransactionReportBusiness;
import com.ladino.simpletest.dao.TransactionDao;
import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionRequest;
import com.ladino.simpletest.model.TransactionSum;
import com.ladino.simpletest.model.TransactionWeekReport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "Transaction methods")
public class TransactionController {

  @Autowired
  TransactionDao transactionDao;

  @Autowired
  TransactionReportBusiness transactionReportBusiness;

  @PostMapping(value = "/user/{userId}/transaction/add")
  @ApiOperation(value = "Adds a new transaction",
      notes = "Adds a new transaction associated to a User ID",
      response = Transaction.class)
  public Transaction addTransaction(
      @ApiParam(value = "The transaction's information") @RequestBody TransactionRequest request,
      @ApiParam(value = "The transaction's user") @PathVariable Integer userId) {
    Transaction transaction = Transaction.builder().amount(request.getAmount())
        .date(request.getDate())
        .description(request.getDescription()).build();
    Transaction response = transactionDao.addTransaction(transaction, userId);
    return response;
  }

  @GetMapping(value = "/user/{userId}/transaction/{transactionId}")
  @ApiOperation(value = "Gets a transaction from DB",
      notes = "Gets a transaction from the database that matches the User ID and Transaction ID",
      response = Transaction.class)
  public Transaction showTransaction(
      @ApiParam(value = "The transaction's user") @PathVariable Integer userId,
      @ApiParam(value = "The transaction's ID", example = "550e8400-e29b-41d4-a716-446655440000")
      @PathVariable String transactionId) {
    Transaction response = transactionDao.findByIdAndUserId(transactionId, userId);
    return response;
  }

  @GetMapping(value = "/user/{userId}/transaction/list")
  @ApiOperation(value = "Gets a list of transactions by User ID",
      notes = "Gets a list of transactions from the database selected by the User ID",
      response = Transaction.class, responseContainer = "List")
  public List<Transaction> listOfTransactions(
      @ApiParam(value = "The transaction's user") @PathVariable Integer userId) {
    List<Transaction> response = transactionDao.findByUserId(userId);
    return response;
  }

  @GetMapping(value = "/user/{userId}/transaction/sum")
  @ApiOperation(value = "Sums all the Transactions' amount by User ID",
      notes = "Obtains all the transactions of the User and sums the amount",
      response = TransactionSum.class)
  public TransactionSum sumOfTransactions(
      @ApiParam(value = "The transaction's user") @PathVariable Integer userId) {
    TransactionSum response = transactionReportBusiness.findSumOfTransactions(userId);
    return response;
  }

  @GetMapping(value = "/user/{userId}/transaction/report")
  public List<TransactionWeekReport> transactionsReport(
      @ApiParam(value = "The transaction's user") @PathVariable Integer userId) {
    List<TransactionWeekReport> response = transactionReportBusiness.getReport(userId);

    return response;
  }
}
