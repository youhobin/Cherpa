package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderProductListRequest {

    private List<ProductUnitCountRequest> orderProducts;

    public OrderProductListRequest(List<ProductUnitCountRequest> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
