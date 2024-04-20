package com.cerpha.cerphaproject.cerpha.order.request;

import lombok.Getter;

import java.util.List;

@Getter
public class AddOrderRequest {

    private Long userId;
    private String deliveryAddress;
    private String deliveryPhone;
    List<AddOrderProductRequest> orderProducts;
}
