package com.cerpha.orderservice.cerpha.wishlist.controller;

import com.cerpha.orderservice.cerpha.wishlist.request.AddWishlistRequest;
import com.cerpha.orderservice.cerpha.wishlist.request.DeleteWishlistRequest;
import com.cerpha.orderservice.cerpha.wishlist.request.UpdateWishlistRequest;
import com.cerpha.orderservice.cerpha.wishlist.response.AllWishlistResponse;
import com.cerpha.orderservice.cerpha.wishlist.service.WishlistService;
import com.cerpha.orderservice.common.dto.ResultDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * Wishlist에 상품 추가
     * @param addWishlistRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<ResultDto> addWishlist(@Valid @RequestBody AddWishlistRequest addWishlistRequest,
                                                 @RequestHeader("userId") Long userId) {
        wishlistService.addWishlist(addWishlistRequest, userId);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    /**
     * Wishlist 리스트 조회
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ResultDto<AllWishlistResponse>> getWishlists(@PathVariable("userId") Long userId) {
        AllWishlistResponse wishlist = wishlistService.getWishlists(userId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, wishlist));
    }

    /**
     * Wishlist 내 상품 수량 변경
     * @param updateWishlistRequest
     * @return
     */
    @PutMapping
    public ResponseEntity<ResultDto> updateWishlistUnitCount(@Valid @RequestBody UpdateWishlistRequest updateWishlistRequest) {
        wishlistService.updateWishlist(updateWishlistRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    /**
     * Wishlist 내 상품 삭제
     * @param deleteWishlistRequest
     * @return
     */
    @DeleteMapping
    public ResponseEntity<ResultDto> deleteWishlist(@Valid @RequestBody DeleteWishlistRequest deleteWishlistRequest) {
        wishlistService.deleteWishlist(deleteWishlistRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    /**
     * Wishlist 삭제
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResultDto> deleteAllWishList(@PathVariable("userId") Long userId) {
        wishlistService.deleteAllWishList(userId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
