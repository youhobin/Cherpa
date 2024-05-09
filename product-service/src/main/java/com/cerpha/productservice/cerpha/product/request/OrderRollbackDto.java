package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRollbackDto {

    private Long orderId;
    private List<ProductUnitCountRequest> orderProducts;

    public OrderRollbackDto(Long orderId, List<ProductUnitCountRequest> orderProducts) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
    }
}
