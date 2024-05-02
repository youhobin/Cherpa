package com.cerpha.productservice.common.client.payment.request;

import lombok.Getter;

@Getter
public class ProcessPaymentRequest {

    private Long userId;
    private Long orderId;

    public ProcessPaymentRequest(Long userId, Long orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }
}
