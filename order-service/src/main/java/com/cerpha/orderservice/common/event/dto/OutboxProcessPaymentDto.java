package com.cerpha.orderservice.common.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutboxProcessPaymentDto {

    private Long id;

    public OutboxProcessPaymentDto(Long id) {
        this.id = id;
    }
}
