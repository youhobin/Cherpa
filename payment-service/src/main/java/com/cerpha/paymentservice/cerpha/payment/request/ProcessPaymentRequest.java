package com.cerpha.paymentservice.cerpha.payment.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ProcessPaymentRequest {

    private Long userId;
    private Long orderId;
}
