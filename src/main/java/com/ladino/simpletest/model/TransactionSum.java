package com.ladino.simpletest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "Transaction Sum", description = "Defines the sum of all the user's transactions amounts")
public class TransactionSum {
    @ApiModelProperty(value = "The transaction's user")
    Integer userId;

    @ApiModelProperty(value = "The user's transactions total amount sum", example = "124.45")
    Double total;
}
