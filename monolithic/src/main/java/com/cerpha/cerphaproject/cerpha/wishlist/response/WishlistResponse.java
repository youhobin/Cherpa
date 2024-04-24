package com.cerpha.cerphaproject.cerpha.wishlist.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WishlistResponse {

    private Long productId;
    private Long unitCount;
    private String productName;
    private Long productPrice;

    @Builder
    public WishlistResponse(Long productId, Long unitCount, String productName, Long productPrice) {
        this.productId = productId;
        this.unitCount = unitCount;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
