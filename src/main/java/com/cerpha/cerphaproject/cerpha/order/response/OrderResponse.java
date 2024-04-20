package com.cerpha.cerphaproject.cerpha.order.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

    private Long userId;
    private Long orderId;
    private String deliveryAddress;
    private String deliveryPhone;
    private Long totalPrice;
    private String status;
    private List<OrderProductResponse> orderProducts;
    private LocalDateTime updatedAt;

    @Builder
    public OrderResponse(Long userId,
                         Long orderId,
                         String deliveryAddress,
                         String deliveryPhone,
                         Long totalPrice,
                         String status,
                         List<OrderProductResponse> orderProducts,
                         LocalDateTime updatedAt) {
        this.userId = userId;
        this.orderId = orderId;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPhone = deliveryPhone;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderProducts = orderProducts;
        this.updatedAt = updatedAt;
    }
}
