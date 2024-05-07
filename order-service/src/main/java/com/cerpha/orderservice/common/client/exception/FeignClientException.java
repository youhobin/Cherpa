package com.cerpha.orderservice.common.client.exception;

import com.cerpha.orderservice.common.exception.ExceptionResponse;
import lombok.Getter;

@Getter
public class FeignClientException extends RuntimeException {

    private final ExceptionResponse exceptionResponse;

    public FeignClientException(ExceptionResponse exceptionResponse) {
        super(exceptionResponse.getMessage());
        this.exceptionResponse = exceptionResponse;
    }
}
