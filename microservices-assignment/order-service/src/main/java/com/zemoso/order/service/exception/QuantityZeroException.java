package com.zemoso.order.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuantityZeroException extends RuntimeException {

    public QuantityZeroException(String message) {
        super(message);
    }
}
