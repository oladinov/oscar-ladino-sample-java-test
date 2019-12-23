package com.ladino.simpletest.business;

import com.ladino.simpletest.dao.TransactionDao;
import com.ladino.simpletest.model.Transaction;
import com.ladino.simpletest.model.TransactionSum;
import com.ladino.simpletest.model.TransactionWeekReport;
import com.ladino.simpletest.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TransactionReportBusinessImpl implements TransactionReportBusiness {

    @Autowired
    TransactionDao transactionDao;

    public List<TransactionWeekReport> getReport(Integer userId) {
        List<Transaction> transactionList = transactionDao.findByUserId(userId);

        Map<LocalDate, List<Transaction>> map = transactionList.stream().collect(Collectors.groupingBy(t -> Utils.DateToLocalDate(t.getDate()).with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY))));
        Map<LocalDate, List<Transaction>> orderedMap = map.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        List<TransactionWeekReport> report = orderedMap.entrySet().stream().map(x -> getReportFromListOfTransactions(x.getValue())).collect(Collectors.toList());

        final Map<Integer, Double> totalAmountMap = new HashMap<Integer, Double>();

        IntStream.range(0, report.size()).forEach(i -> sumTotalAmount(i, totalAmountMap, report.get(i)));

        return report;
    }

    private void sumTotalAmount(int i, Map<Integer, Double> totalAmountMap, TransactionWeekReport transactionWeekReport) {
        if (i == 0) {
            totalAmountMap.put(0, Double.valueOf(transactionWeekReport.getAmount()));
            transactionWeekReport.setTotalAmount("0.00");
        } else {
            Double totalAmount = totalAmountMap.get(i - 1);
            transactionWeekReport.setTotalAmount(String.format("%.2f", totalAmount));
            totalAmountMap.put(i, totalAmount + Double.valueOf(transactionWeekReport.getAmount()));
        }
    }

    private TransactionWeekReport getReportFromListOfTransactions(List<Transaction> transactions) {
        List<Transaction> orderedList = transactions.stream().sorted(Comparator.comparing(Transaction::getDate)).collect(Collectors.toList());

        int quantity = orderedList.size();
        Integer userId = orderedList.get(0).getUserId();
        Double amount = orderedList.stream().mapToDouble(Transaction::getAmount).sum();
        String startDate = getDateFormatted(getFirstDayOfWeekReport(Utils.DateToLocalDate(orderedList.get(0).getDate())));
        String endDate = getDateFormatted(getLastDayOfWeekReport(Utils.DateToLocalDate(orderedList.get(quantity - 1).getDate())));

        return TransactionWeekReport.builder().amount(String.format("%.2f", amount)).startDate(startDate).endDate(endDate).quantity(quantity).userId(userId).build();
    }

    private String getDateFormatted(LocalDate transactionDate) {
        String dayName = transactionDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
        String date = transactionDate.toString();
        StringBuffer sb = new StringBuffer();
        sb.append(date).append(" ").append(dayName);

        return sb.toString();
    }

    private LocalDate getLastDayOfWeekReport(LocalDate dateToLocalDate) {
        if(dateToLocalDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
            return dateToLocalDate;
        }
        LocalDate thursday = dateToLocalDate.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
        LocalDate lastDayOfMonth = dateToLocalDate.with(TemporalAdjusters.lastDayOfMonth());

        return thursday.with(TemporalAdjusters.lastDayOfMonth()).isEqual(lastDayOfMonth) ? thursday : lastDayOfMonth;
    }


    private LocalDate getFirstDayOfWeekReport(LocalDate d) {
        if(d.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            return d;
        }
        LocalDate friday = d.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        LocalDate firstDayOfMonth = d.withDayOfMonth(1);

        return friday.with(TemporalAdjusters.firstDayOfMonth()).isBefore(firstDayOfMonth) ? firstDayOfMonth : friday;
    }

    public TransactionSum findSumOfTransactions(Integer userId) {
        List<Transaction> transactionList = transactionDao.findByUserId(userId);

        Double sum = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        TransactionSum transactionSum = TransactionSum.builder().total(sum).userId(userId).build();

        return transactionSum;
    }
}
