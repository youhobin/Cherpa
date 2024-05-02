package com.cerpha.orderservice.common.client.product.request;

import com.cerpha.orderservice.cerpha.order.request.AddOrderProductRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class DecreaseStockRequest {

    private Long userId;
    private Long orderId;
    private List<AddOrderProductRequest> orderProducts;

    public DecreaseStockRequest(Long userId, Long orderId, List<AddOrderProductRequest> orderProducts) {
        this.userId = userId;
        this.orderId = orderId;
        this.orderProducts = orderProducts;
    }
}
