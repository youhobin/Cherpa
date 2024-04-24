package com.cerpha.orderservice.common.client.product.response;

import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long stock;
    private String producer;

}
