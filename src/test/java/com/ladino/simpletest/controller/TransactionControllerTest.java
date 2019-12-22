package com.ladino.simpletest.controller;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.ladino.simpletest.business.TransactionReportBusiness;
import com.ladino.simpletest.dao.TransactionDao;
import com.ladino.simpletest.exception.TransactionNotFoundException;
import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionControllerTest {

    @Mock
    TransactionDao transactionDao;

    @Mock
    TransactionReportBusiness transactionReportBusiness;

    @InjectMocks
    TransactionController transactionController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddTransaction() {
        Integer userId = 5897;
        Date date = new Date();
        Transaction t = getTransaction(date);
        t.setUserId(userId);
        t.setTransactionId("ABCDE");
        when(transactionDao.addTransaction(any(Transaction.class), anyInt())).thenReturn(t);

        TransactionRequest request = transformTransactionIntoTransactionRequest(t);

        Transaction result = transactionController.addTransaction(request, userId);
        assertEquals("UnitTest", result.getDescription());
        assertEquals(Double.valueOf(123.45), result.getAmount());
        assertEquals(date, result.getDate());
        assertEquals(userId, result.getUserId());
        assertEquals("ABCDE", result.getTransactionId());
    }

    private TransactionRequest transformTransactionIntoTransactionRequest(Transaction t) {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(t.getAmount());
        transactionRequest.setDate(t.getDate());
        transactionRequest.setDescription(t.getDescription());

        return transactionRequest;
    }

    @Test
    public void testShowTransaction() {
        Date d = new Date();
        Integer userId = 5897;
        Transaction t = this.getTransaction(d);
        t.setTransactionId("ABCDE");
        t.setUserId(userId);
        when(transactionDao.findByIdAndUserId(anyString(), anyInt())).thenReturn(t);

        Transaction response = transactionController.showTransaction(userId, "ABCDE");
        assertEquals("ABCDE", response.getTransactionId());
        assertEquals(userId, response.getUserId());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testShowTransactionNotFound() {
        Date d = new Date();
        Integer userId = 5897;
        Transaction t = this.getTransaction(d);
        when(transactionDao.findByIdAndUserId(anyString(), anyInt())).thenThrow(TransactionNotFoundException.class);
        transactionController.showTransaction(userId, "XYZ");
    }

    @Test
    public void testListOfTransaction() {
        Date d = new Date();
        Integer userId = 5897;
        Transaction t = this.getTransaction(d);
        t.setTransactionId("ABCDE");

        List transactions = new ArrayList();
        transactions.add(t);

        when(transactionDao.findByUserId(anyInt())).thenReturn(transactions);

        List response = transactionController.listOfTransactions(userId);

        assertTrue(response.size() == 1);
        assertEquals(t, response.get(0));
    }

    private Transaction getTransaction(Date date) {
        return Transaction.builder().amount(123.45).date(date).description("UnitTest").build();
    }


}
