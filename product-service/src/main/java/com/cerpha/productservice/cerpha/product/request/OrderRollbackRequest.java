package com.cerpha.productservice.cerpha.product.request;

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
