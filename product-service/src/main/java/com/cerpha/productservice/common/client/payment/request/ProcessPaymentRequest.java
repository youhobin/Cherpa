package com.cerpha.productservice.common.client.payment.request;

import com.cerpha.productservice.cerpha.product.request.ProductUnitCountRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class ProcessPaymentRequest {

    private Long userId;
    private Long orderId;
    private List<ProductUnitCountRequest> orderProducts;

    public ProcessPaymentRequest(Long userId, Long orderId, List<ProductUnitCountRequest> orderProducts) {
        this.userId = userId;
        this.orderId = orderId;
        this.orderProducts = orderProducts;
    }
}
