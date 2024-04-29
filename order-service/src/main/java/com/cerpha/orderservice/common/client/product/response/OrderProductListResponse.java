package com.cerpha.orderservice.common.client.product.response;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderProductListResponse {

    private List<AddOrderProductResponse> products;
}
