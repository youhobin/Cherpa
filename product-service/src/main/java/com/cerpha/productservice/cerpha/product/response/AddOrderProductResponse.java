package com.cerpha.productservice.cerpha.product.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddOrderProductResponse {

    private Long productId;
    private Long unitCount;
    private Long price;

    @Builder
    public AddOrderProductResponse(Long productId, Long unitCount, Long price) {
        this.productId = productId;
        this.unitCount = unitCount;
        this.price = price;
    }
}
