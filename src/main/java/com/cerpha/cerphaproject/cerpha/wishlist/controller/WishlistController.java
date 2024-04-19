package com.cerpha.cerphaproject.cerpha.wishlist.controller;

import com.cerpha.cerphaproject.cerpha.wishlist.request.AddWishlistRequest;
import com.cerpha.cerphaproject.cerpha.wishlist.request.DeleteWishlistRequest;
import com.cerpha.cerphaproject.cerpha.wishlist.request.UpdateWishlistRequest;
import com.cerpha.cerphaproject.cerpha.wishlist.response.AllWishlistResponse;
import com.cerpha.cerphaproject.cerpha.wishlist.service.WishlistService;
import com.cerpha.cerphaproject.common.dto.ResultDto;
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

    @PostMapping("/{userId}")
    public ResponseEntity<ResultDto> addWishlist(@PathVariable("userId") Long userId,
                                                 @Valid @RequestBody AddWishlistRequest addWishlistRequest) {
        wishlistService.addWishlist(userId, addWishlistRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResultDto<AllWishlistResponse>> getWishlists(@PathVariable("userId") Long userId) {
        AllWishlistResponse wishlist = wishlistService.getWishlists(userId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, wishlist));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResultDto> updateWishlistUnitCount(@PathVariable("userId") Long userId,
                                                             @Valid @RequestBody UpdateWishlistRequest updateWishlistRequest) {
        wishlistService.updateWishlist(userId, updateWishlistRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResultDto> deleteWishlist(@PathVariable("userId") Long userId,
                                                    @Valid @RequestBody DeleteWishlistRequest deleteWishlistRequest) {
        wishlistService.deleteWishlist(userId, deleteWishlistRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
