package com.cerpha.paymentservice.common.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCancelEvent {

    private Long orderId;

    public OrderCancelEvent(Long orderId) {
        this.orderId = orderId;
    }
}
