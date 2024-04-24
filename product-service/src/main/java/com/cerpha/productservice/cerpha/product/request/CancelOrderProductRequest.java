package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;

@Getter
public class CancelOrderProductRequest {

    private Long productId;
    private Long unitCount;

}
