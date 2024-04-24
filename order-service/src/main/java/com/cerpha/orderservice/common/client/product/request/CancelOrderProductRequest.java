package com.cerpha.orderservice.common.client.product.request;

import lombok.Getter;

@Getter
public class CancelOrderProductRequest {

    private Long productId;
    private Long unitCount;

    public CancelOrderProductRequest(Long productId, Long unitCount) {
        this.productId = productId;
        this.unitCount = unitCount;
    }
}
