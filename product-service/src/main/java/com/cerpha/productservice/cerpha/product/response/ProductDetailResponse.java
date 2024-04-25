package com.cerpha.productservice.cerpha.product.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductDetailResponse {

    private Long productId;
    private String productName;
    private Long productPrice;

    @Builder
    public ProductDetailResponse(Long productId, String productName, Long productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
