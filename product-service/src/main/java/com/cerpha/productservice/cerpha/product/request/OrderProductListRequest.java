package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderProductListRequest {

    private List<AddOrderProductRequest> orderProducts;

    public OrderProductListRequest(List<AddOrderProductRequest> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
