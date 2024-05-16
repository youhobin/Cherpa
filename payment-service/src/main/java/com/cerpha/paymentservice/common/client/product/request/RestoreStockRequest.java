package com.cerpha.paymentservice.common.client.product.request;

import com.cerpha.paymentservice.cerpha.payment.request.ProductUnitCountRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class RestoreStockRequest {

    List<ProductUnitCountRequest> orderProducts;

    public RestoreStockRequest(List<ProductUnitCountRequest> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
