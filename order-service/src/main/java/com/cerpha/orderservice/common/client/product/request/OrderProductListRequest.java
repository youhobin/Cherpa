package com.cerpha.orderservice.common.client.product.request;

import com.cerpha.orderservice.cerpha.order.request.ProductUnitCountRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderProductListRequest {

    private List<ProductUnitCountRequest> orderProducts;

    public OrderProductListRequest(List<ProductUnitCountRequest> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
