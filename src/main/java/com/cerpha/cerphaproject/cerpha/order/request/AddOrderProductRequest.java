package com.cerpha.cerphaproject.cerpha.order.request;

import lombok.Getter;

@Getter
public class AddOrderProductRequest {

    private Long productId;
    private Long unitCount;

}
