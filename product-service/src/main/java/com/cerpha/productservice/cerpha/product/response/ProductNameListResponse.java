package com.cerpha.productservice.cerpha.product.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductNameListResponse {

    List<ProductNameResponse> nameResponses;

    public ProductNameListResponse(List<ProductNameResponse> nameResponses) {
        this.nameResponses = nameResponses;
    }
}
