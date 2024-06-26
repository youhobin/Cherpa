package com.cerpha.orderservice.common.client.product.response;

import lombok.Getter;

@Getter
public class OrderProductDetailResponse {

    private Long productId;
    private Long unitCount;
    private Long price;
    private String productName;

}
