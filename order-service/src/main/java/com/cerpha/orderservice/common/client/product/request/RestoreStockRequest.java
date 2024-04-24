package com.cerpha.orderservice.common.client.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class RestoreStockRequest {

    List<CancelOrderProductRequest> orderProducts;

    public RestoreStockRequest(List<CancelOrderProductRequest> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
