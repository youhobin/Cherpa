package com.cerpha.orderservice.common.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutboxRestoreStockDto {

    private Long id;

    public OutboxRestoreStockDto(Long id) {
        this.id = id;
    }
}
