package com.ladino.simpletest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Builder
@Data
@ApiModel(value = "Transaction Week Report", description = "Report of user's transactions by week")
public class TransactionWeekReport {

    @ApiModelProperty(value = "The transaction report's user")
    Integer userId;
    @ApiModelProperty(value = "The start date of the week report")
    String startDate;
    @ApiModelProperty(value = "The end date of the week report")
    String endDate;
    @ApiModelProperty(value = "The number of transactions in the week reported")
    Integer quantity;
    @ApiModelProperty(value = "The total amount of transactions in the week reported")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    String amount;
    @ApiModelProperty(value = "The total accumulated amount of the report")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    String totalAmount;
}
