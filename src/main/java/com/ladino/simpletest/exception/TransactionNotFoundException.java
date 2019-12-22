package com.ladino.simpletest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "transaction not found")
public class TransactionNotFoundException extends RuntimeException {
}
