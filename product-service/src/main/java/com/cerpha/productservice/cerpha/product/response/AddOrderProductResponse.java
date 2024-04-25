package com.cerpha.productservice.cerpha.product.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddOrderProductResponse {

    private Long productId;
    private Long unitCount;
    private Long price;
    private String productName;

    @Builder
    public AddOrderProductResponse(Long productId, Long unitCount, Long price, String productName) {
        this.productId = productId;
        this.unitCount = unitCount;
        this.price = price;
        this.productName = productName;
    }
}
