package com.cerpha.cerphaproject.cerpha.product.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long stock;
    private String producer;

    @Builder
    public ProductResponse(Long id, String name, String description, Long price, Long stock, String producer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.producer = producer;
    }
}
