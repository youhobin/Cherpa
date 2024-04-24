package com.cerpha.orderservice.common.client.product.request;

import com.cerpha.orderservice.cerpha.order.request.AddOrderProductRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class DecreaseStockRequest {

    private List<AddOrderProductRequest> orderProducts;

    public DecreaseStockRequest(List<AddOrderProductRequest> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
