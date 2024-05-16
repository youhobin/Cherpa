package com.cerpha.productservice.cerpha.product.request;

import com.cerpha.productservice.cerpha.product.domain.Product;
import lombok.Getter;

@Getter
public class AddProductRequest {

    private String name;
    private String description;
    private Long stock;
    private Long price;
    private String producer;

}
