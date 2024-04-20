package com.cerpha.cerphaproject.cerpha.refund.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RefundRequest {

    @NotNull(message = "유저 ID가 필요합니다.")
    private Long userId;

    @NotNull(message = "주문 ID가 필요합니다.")
    private Long orderId;

    @NotNull(message = "반품 사유는 NULL 일 수 없습니다.")
    private String refundReason;

}
