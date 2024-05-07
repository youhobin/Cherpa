package com.cerpha.productservice.cerpha.product.response;

import lombok.Getter;

@Getter
public class ProductStockResponse {

    private Long productId;
    private Long stock;

    public ProductStockResponse(Long productId, Long stock) {
        this.productId = productId;
        this.stock = stock;
    }
}
