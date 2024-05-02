package com.cerpha.paymentservice.cerpha.payment.request;

import lombok.Getter;

@Getter
public class ProcessPaymentRequest {

    private Long userId;
    private Long orderId;
}
