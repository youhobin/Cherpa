package com.cerpha.paymentservice.cerpha.payment.request;

import lombok.Getter;

@Getter
public class OrderRollbackRequest {

    private Long orderId;
    private boolean fullRollback;

    public OrderRollbackRequest(Long orderId, boolean fullRollback) {
        this.orderId = orderId;
        this.fullRollback = fullRollback;
    }
}
