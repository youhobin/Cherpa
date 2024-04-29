package com.cerpha.cerphaproject.cerpha.order.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderProductResponse {

    private Long productId;
    private String productName;
    private Long unitCount;

    @Builder
    public OrderProductResponse(Long productId, String productName, Long unitCount) {
        this.productId = productId;
        this.productName = productName;
        this.unitCount = unitCount;
    }
}
