package com.cerpha.orderservice.cerpha.wishlist.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeleteWishlistRequest {

    @NotNull(message = "유저 ID가 필요합니다.")
    private Long userId;

    @NotNull(message = "Wishlist 번호가 필요합니다.")
    @Min(value = 1, message = "Wishlist 번호가 1보다 작을 수 없습니다.")
    private Long wishlistId;
}
