package com.cerpha.cerphaproject.common.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private int status;
    private String code;
    private String message;

    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getStatus();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
