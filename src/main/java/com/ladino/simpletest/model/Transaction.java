package com.ladino.simpletest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Data
@ApiModel(value = "Transaction", description = "Defines a Transaction's amount, date, and description")
public class Transaction {

  @Id
  @ApiModelProperty(value = "The transaction's ID", example = "550e8400-e29b-41d4-a716-446655440000")
  String transactionId;

  @ApiModelProperty(value = "The transaction's amount", example = "124.45")
  Double amount;

  @ApiModelProperty(value = "The transaction's description", example = "Transaction Description")
  String description;

  @ApiModelProperty(value = "The transaction's date", example = "2019-12-11")
  Date date;

  @ApiModelProperty(value = "The transaction's user")
  Integer userId;
}
