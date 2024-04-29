package com.cerpha.productservice.cerpha.product.request;

import lombok.Getter;

import java.util.List;

@Getter
public class RestoreStockRequest {

    List<CancelOrderProductRequest> orderProducts;

}
