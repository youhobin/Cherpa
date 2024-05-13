package com.cerpha.orderservice.common.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutboxDecreaseStockDto {

    private Long id;

    public OutboxDecreaseStockDto(Long id) {
        this.id = id;
    }
}
