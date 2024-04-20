package com.cerpha.cerphaproject.cerpha.order.response;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderListResponse {

    List<OrderResponse> orders;

    public OrderListResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }
}
