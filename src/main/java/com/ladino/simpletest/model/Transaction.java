package com.ladino.simpletest.model;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class Transaction {

    @Id
    String transactionId;

    Double amount;

    String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;

    Integer userId;
}
