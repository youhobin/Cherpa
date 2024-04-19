package com.cerpha.cerphaproject.cerpha.product.response;

import jakarta.persistence.Column;
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
