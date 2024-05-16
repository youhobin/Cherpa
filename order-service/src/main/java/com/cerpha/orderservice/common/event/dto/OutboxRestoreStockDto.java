package com.cerpha.orderservice.common.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutboxRestoreStockDto {

    private Long id;
    private String event;

    public OutboxRestoreStockDto(Long id, String event) {
        this.id = id;
        this.event = event;
    }
}
