package com.cerpha.orderservice.common.client.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class WishlistProductRequest {

    private List<ProductRequest> products;

    public WishlistProductRequest(List<ProductRequest> products) {
        this.products = products;
    }
}
