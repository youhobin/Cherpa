package com.cerpha.productservice.cerpha.product.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddOrderProductRequest {

    private Long productId;
    private Long unitCount;

}