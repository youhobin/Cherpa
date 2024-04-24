package com.cerpha.productservice.cerpha.product.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WishlistResponse {

    private Long wishlistId;
    private Long productId;
    private Long unitCount;
    private String productName;
    private Long productPrice;

    @Builder
    public WishlistResponse(Long wishlistId, Long productId, Long unitCount, String productName, Long productPrice) {
        this.wishlistId = wishlistId;
        this.productId = productId;
        this.unitCount = unitCount;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
