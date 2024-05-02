package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class DecreaseStockRequest {

    private Long userId;
    private Long orderId;
    private List<ProductUnitCountRequest> orderProducts;
}
