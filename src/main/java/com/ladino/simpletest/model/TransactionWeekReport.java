package com.ladino.simpletest.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransactionWeekReport {

    Integer userId;
    String startDate;
    String endDate;
    Integer quantity;
    Double amount;
    Double totalAmount;

}
