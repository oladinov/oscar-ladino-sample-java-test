package com.ladino.simpletest.business;

import com.ladino.simpletest.model.TransactionSum;
import com.ladino.simpletest.model.TransactionWeekReport;

import java.util.List;

public interface TransactionReportBusiness {
    public List<TransactionWeekReport> getReport(Integer userId);
    public TransactionSum findSumOfTransactions(Integer userId);

}
