package com.cerpha.orderservice.common.client.product.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductRequest {

    private Long wishlistId;
    private Long productId;
    private Long unitCount;

    @Builder
    public ProductRequest(Long wishlistId, Long productId, Long unitCount) {
        this.wishlistId = wishlistId;
        this.productId = productId;
        this.unitCount = unitCount;
    }
}
