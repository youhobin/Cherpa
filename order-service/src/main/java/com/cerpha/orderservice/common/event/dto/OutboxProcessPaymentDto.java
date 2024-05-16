package com.cerpha.orderservice.common.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutboxProcessPaymentDto {

    private Long id;
    private String event;

    public OutboxProcessPaymentDto(Long id, String event) {
        this.id = id;
        this.event = event;
    }
}
