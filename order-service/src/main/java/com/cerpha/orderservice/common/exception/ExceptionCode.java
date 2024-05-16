package com.cerpha.orderservice.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    // common
    AES_ENCRYPTION_ERROR(500, "C001", "Encryption 오류"),
    INVALID_REQUEST(400, "C002", "유효하지 않은 입력 값입니다."),
    INVALID_CREDENTIALS(401, "C003", "사용자 아이디 또는 비밀번호가 잘못되었습니다."),
    REDIS_CONNECTION_FAIL(500, "C004", "레디스 연결 실패"),
    FORBIDDEN(403, "C005", "권한이 없습니다."),
    EVENT_NOT_FOUND(404, "C006", "이벤트를 찾을 수 없습니다."),

    // user
    NOT_FOUND_USER(404, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATED_EMAIL(400,"U002","중복된 이메일 입니다."),
    NOT_EQUAL_NEW_PASSWORD(400, "U003", "변경할 비밀번호가 일치하지 않습니다."),
    NOT_EQUAL_PREV_PASSWORD(400, "U004", "기존 비밀번호와 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(400, "U005", "유효하지 않은 Refresh Token 입니다."),
    INVALID_AUTH_NUMBER(400,"U006", "잘못된 인증 번호 입니다."),
    NOT_AUTH_EMAIL(400, "U007", "인증 되지 않은 이메일입니다."),

    // product
    NOT_FOUND_PRODUCT(404, "P001", "상품을 찾을 수 없습니다."),
    OUT_OF_STOCK(404, "P002", "상품의 재고가 부족합니다."),

    // wishlist
    DUPLICATED_WISHLIST_PRODUCT(400, "W001", "이미 WishList에 등록된 상품입니다."),
    NOT_FOUND_WISHLIST(404, "W002", "Wishlist에서 해당 상품을 찾을 수 없습니다."),
    NOT_OWN_WISHLIST(400, "W003", "이 Wishlist는 해당 유저가 소유하지 않았습니다."),
    NOT_AVAILABLE_ADD_WISHLIST(500, "W004", "현재 Wishlist에 상품을 추가할 수 없습니다."),

    // order
    NOT_FOUND_ORDER(404, "O001", "해당 주문을 찾을 수 없습니다."),
    NOT_AVAILABLE_CANCEL(400, "O002", "주문을 취소할 수 없는 상태입니다."),
    NOT_AVAILABLE_REFUND(400, "O003", "반품할 수 없는 주문입니다."),
    NOT_AVAILABLE_ORDER(500, "O004", "현재 주문이 불가합니다."),
    NOT_AVAILABLE_PAYMENT(500, "O005", "결제를 완료할 수 없는 상태입니다."),

    // payment
    CHANGE_MIND(400, "PM001", "고객 단순 변심 에러");

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
