package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;

@Getter
public class ProductUnitCountRequest {

    private Long productId;
    private Long unitCount;

    public ProductUnitCountRequest(Long productId, Long unitCount) {
        this.productId = productId;
        this.unitCount = unitCount;
    }
}
