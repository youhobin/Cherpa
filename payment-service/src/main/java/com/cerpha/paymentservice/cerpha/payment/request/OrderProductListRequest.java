package com.cerpha.paymentservice.cerpha.payment.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderProductListRequest {

    private List<AddOrderProductRequest> orderProducts;

}
