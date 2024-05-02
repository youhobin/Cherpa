package com.cerpha.paymentservice.cerpha.payment.domain;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    PAYMENT_WAIT, PAYMENT_CANCEL, PAYMENT_COMPLETED
}
