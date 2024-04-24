package com.cerpha.orderservice.common.client.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class GetProductsNameRequest {

    private List<Long> productIds;

    public GetProductsNameRequest(List<Long> productIds) {
        this.productIds = productIds;
    }
}
