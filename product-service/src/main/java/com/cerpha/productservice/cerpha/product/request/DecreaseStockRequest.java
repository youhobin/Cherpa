package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class DecreaseStockRequest {

    private List<AddOrderProductRequest> orderProducts;
}
