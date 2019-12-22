package com.ladino.simpletest.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class TransactionSum {
    @Id
    Integer userId;
    Double total;
}
