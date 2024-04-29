package com.cerpha.productservice.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InvalidError {

    private String fieldName;
    private String rejectValue;
    private String message;

    @Builder
    public InvalidError(String fieldName, String rejectValue, String message) {
        this.fieldName = fieldName;
        this.rejectValue = rejectValue;
        this.message = message;
    }
}
