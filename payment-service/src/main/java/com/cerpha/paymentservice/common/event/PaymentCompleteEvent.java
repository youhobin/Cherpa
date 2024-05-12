package com.cerpha.paymentservice.common.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCompleteEvent {

    private Long orderId;

    public PaymentCompleteEvent(Long orderId) {
        this.orderId = orderId;
    }
}
