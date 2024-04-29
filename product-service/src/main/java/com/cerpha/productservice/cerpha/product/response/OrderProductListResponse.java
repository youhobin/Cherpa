package com.cerpha.productservice.cerpha.product.response;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderProductListResponse {

    private List<AddOrderProductResponse> products;

    public OrderProductListResponse(List<AddOrderProductResponse> products) {
        this.products = products;
    }
}
