package com.ladino.simpletest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import java.util.Date;

@Data
@ApiModel(value = "Transaction input", description = "Defines the necesary input to create a new Transaction")
public class TransactionRequest {
    @ApiModelProperty(value = "The transaction's amount", example = "124.45")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Valid
    Double amount;

    @ApiModelProperty(value = "The transaction's description", example = "Transaction Description")
    String description;

    @ApiModelProperty(value = "The transaction's date", example = "2019-12-11")
    @DateTimeFormat(pattern="dd-MM-yyyy")
    @Valid
    Date date;
}
