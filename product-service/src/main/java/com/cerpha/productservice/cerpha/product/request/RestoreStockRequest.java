package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RestoreStockRequest {

    List<ProductUnitCountRequest> orderProducts;

    public RestoreStockRequest(List<ProductUnitCountRequest> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
