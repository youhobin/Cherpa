package com.cerpha.cerphaproject.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ExceptionResponse {

    private int status;
    private String code;
    private String message;
    private List<InvalidError> errorFieldList;

    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getStatus();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public ExceptionResponse(ExceptionCode exceptionCode, List<InvalidError> errorFieldList) {
        this(exceptionCode);
        this.errorFieldList = errorFieldList;
    }
}
