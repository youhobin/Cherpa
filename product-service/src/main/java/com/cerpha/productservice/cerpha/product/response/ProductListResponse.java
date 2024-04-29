package com.cerpha.productservice.cerpha.product.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductListResponse {

    private Long id;
    private String name;
    private Long price;
    private String producer;

    @Builder
    public ProductListResponse(Long id, String name, Long price, String producer) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.producer = producer;
    }
}
