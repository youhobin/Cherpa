package com.cerpha.paymentservice.cerpha.payment.request;

import lombok.Getter;

@Getter
public class AddOrderProductRequest {

    private Long productId;
    private Long unitCount;

}
