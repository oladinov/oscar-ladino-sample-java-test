package com.ladino.simpletest.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TransactionRequest {
    Double amount;

    String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;
}
