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

    /**
     * Obtains a report of transactions by week, where the first day of the week is friday or the
     * first day of the month. The last day of the week is thursday or the last day of the month.
     * The amount is the sum of all the transactions in the week, and the total amount is the sum of
     * previous weeks not including the current one.
     * @param userId The user of the transactions.
     * @return A list of every week transactions report.
     */
  public List<TransactionWeekReport> getReport(Integer userId) {
    List<Transaction> transactionList = transactionDao.findByUserId(userId);

    Map<LocalDate, List<Transaction>> map = transactionList.stream()
        .collect(Collectors.groupingBy(t -> getFirstDayOfWeekReport(Utils.DateToLocalDate(t.getDate()))));
    Map<LocalDate, List<Transaction>> orderedMap = map.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    List<TransactionWeekReport> report = orderedMap.values().stream()
        .map(this::getReportFromListOfTransactions).collect(Collectors.toList());

    final Map<Integer, Double> totalAmountMap = new HashMap<>();

    IntStream.range(0, report.size())
        .forEach(i -> sumTotalAmount(i, totalAmountMap, report.get(i)));

    return report;
  }

    /**
     * Obtains the total amount of previous week reports, and stores it in current week's report.
     * Then sums the week's amount and adds it to a map to be used for the next week's report.
     * Thus ensuring the amount is not recalculated for every week report.
     * @param i The number of the week.
     * @param totalAmountMap Map where the total amount is being stored.
     * @param transactionWeekReport The current week report.
     */
  private void sumTotalAmount(int i, Map<Integer, Double> totalAmountMap,
      TransactionWeekReport transactionWeekReport) {
    if (i == 0) {
      totalAmountMap.put(0, Double.valueOf(transactionWeekReport.getAmount()));
      transactionWeekReport.setTotalAmount("0.00");
    } else {
      Double totalAmount = totalAmountMap.get(i - 1);
      transactionWeekReport.setTotalAmount(String.format("%.2f", totalAmount));
      totalAmountMap.put(i, totalAmount + Double.parseDouble(transactionWeekReport.getAmount()));
    }
  }

    /**
     * Creates a new week report with the week's transactions.
     * @param transactions List with the week's transactions.
     * @return The week report.
     */
  private TransactionWeekReport getReportFromListOfTransactions(List<Transaction> transactions) {
    List<Transaction> orderedList = transactions.stream()
        .sorted(Comparator.comparing(Transaction::getDate)).collect(Collectors.toList());

    int quantity = orderedList.size();
    Integer userId = orderedList.get(0).getUserId();
    Double amount = orderedList.stream().mapToDouble(Transaction::getAmount).sum();
    String startDate = getDateFormatted(
        getFirstDayOfWeekReport(Utils.DateToLocalDate(orderedList.get(0).getDate())));
    String endDate = getDateFormatted(
        getLastDayOfWeekReport(Utils.DateToLocalDate(orderedList.get(quantity - 1).getDate())));

    return TransactionWeekReport.builder().amount(String.format("%.2f", amount))
        .startDate(startDate).endDate(endDate).quantity(quantity).userId(userId).build();
  }

    /**
     * Formats the date to yyyy-MM-dd DayOfTheWeek
     * @param transactionDate The transaction's date.
     * @return The date formatted.
     */
  private String getDateFormatted(LocalDate transactionDate) {
    String dayName = transactionDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
    String date = transactionDate.toString();

    return date + " " + dayName;
  }

    /**
     * Obtains the last day of the week report. The last day is the next Thursday to the
     * transaction's date. If the last day of the month is before next Thursday, it's used instead.
     * @param dateToLocalDate Transaction's date.
     * @return The last fay of the week.
     */
  private LocalDate getLastDayOfWeekReport(LocalDate dateToLocalDate) {
    if (dateToLocalDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
      return dateToLocalDate;
    }
    LocalDate thursday = dateToLocalDate.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
    LocalDate lastDayOfMonth = dateToLocalDate.with(TemporalAdjusters.lastDayOfMonth());

    return lastDayOfMonth.isBefore(thursday) ? lastDayOfMonth : thursday;
  }

    /**
     * Obtains the first day of the week report. The first day is the previous Friday. If the
     * previous friday is before the first day of the month, the latter is used instead.
     * @param d The transaction's date.
     * @return The first day of the week.
     */
  private LocalDate getFirstDayOfWeekReport(LocalDate d) {
    if (d.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
      return d;
    }
    LocalDate friday = d.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
    LocalDate firstDayOfMonth = d.withDayOfMonth(1);

    return friday.isBefore(firstDayOfMonth) ? firstDayOfMonth : friday;
  }

    /**
     * Obtains all the transactions associated with the user id. Then sum the amount of each transaction.
     * @param userId The user of the transactions.
     * @return object with user id and total sum.
     */
  public TransactionSum findSumOfTransactions(Integer userId) {
    List<Transaction> transactionList = transactionDao.findByUserId(userId);

    Double sum = transactionList.stream().mapToDouble(Transaction::getAmount).sum();

    return TransactionSum.builder().total(sum).userId(userId).build();
  }
}
