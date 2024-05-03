package com.cerpha.productservice.common.client.exception;

import com.cerpha.productservice.common.exception.ExceptionResponse;
import lombok.Getter;

@Getter
public class FeignClientException extends RuntimeException {

    private final ExceptionResponse exceptionResponse;

    public FeignClientException(ExceptionResponse exceptionResponse) {
        super(exceptionResponse.getMessage());
        this.exceptionResponse = exceptionResponse;
    }
}
