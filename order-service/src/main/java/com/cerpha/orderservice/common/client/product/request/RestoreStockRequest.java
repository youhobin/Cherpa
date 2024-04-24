package com.cerpha.orderservice.common.client.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class RestoreStockRequest {

    List<ProductUnitCountRequest> orderProducts;

    public RestoreStockRequest(List<ProductUnitCountRequest> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
