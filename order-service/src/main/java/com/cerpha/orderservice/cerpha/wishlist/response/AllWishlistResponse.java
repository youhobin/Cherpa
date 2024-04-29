package com.cerpha.orderservice.cerpha.wishlist.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AllWishlistResponse {

    private Long userId;
    private Long totalPrice;
    List<WishlistResponse> wishlist;

    @Builder
    public AllWishlistResponse(Long userId, Long totalPrice, List<WishlistResponse> wishlist) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.wishlist = wishlist;
    }
}
