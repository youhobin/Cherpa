package com.cerpha.paymentservice.cerpha.payment.request;

import lombok.Getter;

@Getter
public class CompletePaymentRequest {

    private Long orderId;
    private String paymentMethod;
}
