package com.ladino.simpletest.dao;

import com.ladino.simpletest.exception.TransactionNotFoundException;
import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionSum;
import com.ladino.simpletest.repository.TransactionRepository;
import com.ladino.simpletest.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TransactionDaoImplTest {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionDaoImpl transactionDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddTransaction() {
        Date date = new Date();
        Integer userId = 5897;
        Transaction t = Transaction.builder().description("UnitTest").amount(123.45D).date(date).build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(any(Transaction.class));
        Transaction response = transactionDao.addTransaction(t, userId);

        assertEquals(userId, response.getUserId());
        assertEquals("UnitTest", response.getDescription());
        assertNotNull(response.getTransactionId());
    }

    @Test
    public void testFindByIdAndUserId() {

        Date date = new Date();
        Integer userId = 5897;
        String transactionId = Utils.generateUUID().toString();
        Transaction t = getUnitTestTransaction(date, userId, transactionId);

        Optional optional = Optional.of(t);

        when(transactionRepository.findByTransactionIdAndUserId(anyString(), anyInt())).thenReturn(optional);

        Transaction response = transactionDao.findByIdAndUserId(transactionId, userId);

        assertEquals(transactionId, response.getTransactionId());
        assertEquals(userId, response.getUserId());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testFindByUserIdAndTransactionIdNotFound() {
        Transaction t = null;
        Optional optional = Optional.ofNullable(t);

        when(transactionRepository.findByTransactionIdAndUserId(anyString(), anyInt())).thenReturn(optional);

        transactionDao.findByIdAndUserId("ABC", 123);
    }

    @Test
    public void testFindByUserId() {
        Date date = new Date();
        Integer userId = 5897;
        String transactionId1 = Utils.generateUUID();
        String transactionId2 = Utils.generateUUID();

        Transaction t1 = getUnitTestTransaction(date, userId, transactionId1);
        Transaction t2 = getUnitTestTransaction(date, userId, transactionId2);

        List<Transaction> list = Stream.of(t1, t2).collect(Collectors.toList());
        when(transactionRepository.findByUserId(anyInt())).thenReturn(list);

        List<Transaction> response = transactionDao.findByUserId(userId);

        assertTrue(!response.isEmpty());
        assertSame(list.get(0), list.get(0));
    }

    @Test
    public void testFindSunOfTransactions() {
        TransactionSum sum = TransactionSum.builder().total(99.89).userId(5897).build();

        when(transactionRepository.sumTransactionsByUser(anyInt())).thenReturn(sum);

        TransactionSum response = transactionDao.findSumOfTransactions(5897);

        assertEquals(Integer.valueOf(5897), response.getUserId());
        assertEquals(Double.valueOf(99.89), response.getTotal());
    }

    private Transaction getUnitTestTransaction(Date date, Integer userId, String transactionId) {
        return Transaction.builder().description("UnitTest").amount(123.45D).date(date).userId(userId)
                .transactionId(transactionId).build();
    }
}
