package com.cerpha.cerphaproject.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    NOT_FOUND_USER(404, "U001", "사용자를 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
