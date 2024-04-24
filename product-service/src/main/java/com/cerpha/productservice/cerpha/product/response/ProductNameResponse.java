package com.cerpha.productservice.cerpha.product.response;

import lombok.Getter;

@Getter
public class ProductNameResponse {

    private Long productId;
    private String name;

    public ProductNameResponse(Long productId, String name) {
        this.productId = productId;
        this.name = name;
    }
}
