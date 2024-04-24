package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class GetProductsNameRequest {

    private List<Long> productIds;
}
