package com.cerpha.cerphaproject.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    //common
    AES_ENCRYPTION_ERROR(500, "C001", "Encryption 오류"),

    // user
    NOT_FOUND_USER(404, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATED_EMAIL(400,"U002","중복된 이메일 입니다."),
    NOT_EQUAL_NEW_PASSWORD(400, "U003", "변경할 비밀번호가 일치하지 않습니다."),
    NOT_EQUAL_PREV_PASSWORD(400, "U004", "기존 비밀번호와 일치하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
