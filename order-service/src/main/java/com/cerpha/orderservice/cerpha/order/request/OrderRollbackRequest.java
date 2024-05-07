package com.cerpha.orderservice.cerpha.order.request;

import lombok.Getter;

@Getter
public class OrderRollbackRequest {

    private Long orderId;
    private boolean fullRollback;

}
