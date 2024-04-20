package com.cerpha.cerphaproject.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    // common
    AES_ENCRYPTION_ERROR(500, "C001", "Encryption 오류"),
    INVALID_REQUEST(400, "C002", "유효하지 않은 입력 값입니다."),

    // user
    NOT_FOUND_USER(404, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATED_EMAIL(400,"U002","중복된 이메일 입니다."),
    NOT_EQUAL_NEW_PASSWORD(400, "U003", "변경할 비밀번호가 일치하지 않습니다."),
    NOT_EQUAL_PREV_PASSWORD(400, "U004", "기존 비밀번호와 일치하지 않습니다."),

    // product
    NOT_FOUND_PRODUCT(404, "P001", "상품을 찾을 수 없습니다."),

    // wishlist
    DUPLICATED_WISHLIST_PRODUCT(400, "W001", "이미 WishList에 등록된 상품입니다."),
    NOT_FOUND_WISHLIST(404, "W002", "WishList에서 해당 상품을 찾을 수 없습니다."),

    // order
    NOT_FOUND_ORDER(404, "O001", "해당 주문을 찾을 수 없습니다."),
    NOT_AVAILABLE_CANCEL(400, "O002", "주문을 취소할 수 없는 상태입니다.");

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
