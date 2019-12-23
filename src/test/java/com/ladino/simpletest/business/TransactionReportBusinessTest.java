package com.ladino.simpletest.business;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ladino.simpletest.dao.TransactionDao;
import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionSum;
import com.ladino.simpletest.model.TransactionWeekReport;
import com.ladino.simpletest.util.Utils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionReportBusinessTest {
    @Mock
    TransactionDao transactionDao;

    @InjectMocks
    TransactionReportBusinessImpl transactionReportBusiness;

    @Test
    public void findSumOfTransactions() {
        Date date = new Date();
        Integer userId = 5897;
        String transactionId1 = Utils.generateUUID();
        String transactionId2 = Utils.generateUUID();

        Transaction t1 = getUnitTestTransaction(date, userId, transactionId1);
        Transaction t2 = getUnitTestTransaction(date, userId, transactionId2);

        List<Transaction> list = Stream.of(t1, t2).collect(Collectors.toList());
        when(transactionDao.findByUserId(anyInt())).thenReturn(list);

        TransactionSum response = transactionReportBusiness.findSumOfTransactions(userId);

        assertEquals(Integer.valueOf(5897), response.getUserId());
        assertEquals(Double.valueOf(246.90), response.getTotal());
    }

    @Test
    public void testGetReport() {
        List<Transaction> list = this.setUpListOfTransactions();

        when(transactionDao.findByUserId(anyInt())).thenReturn(list);

        List<TransactionWeekReport> response = transactionReportBusiness.getReport(1112);

        assertThat(response, Matchers.hasSize(3));
        assertThat(response, Matchers.everyItem(Matchers.hasProperty("userId", Matchers.equalTo(1112))));
        assertThat(response.get(2), Matchers.hasProperty("totalAmount", Matchers.equalTo("225.65")));
        assertThat(response.get(1), Matchers.hasProperty("endDate", Matchers.equalTo("2019-12-19 Thursday")));
    }

    private Transaction getUnitTestTransaction(Date date, Integer userId, String transactionId) {
        return Transaction.builder().description("UnitTest").amount(123.45D).date(date).userId(userId)
                .transactionId(transactionId).build();
    }

    private List<Transaction> setUpListOfTransactions() {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Transaction> transactionList = null;
        try {
            transactionList = Arrays.asList(objectMapper.readValue(new File("src/test/resources/json/listOfTransactions.json"), Transaction[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactionList;
    }
}