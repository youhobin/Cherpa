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

    @PostMapping
    public ResponseEntity<ResultDto> addWishlist(@Valid @RequestBody AddWishlistRequest addWishlistRequest) {
        wishlistService.addWishlist(addWishlistRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResultDto<AllWishlistResponse>> getWishlists(@PathVariable("userId") Long userId) {
        AllWishlistResponse wishlist = wishlistService.getWishlists(userId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, wishlist));
    }

    @PutMapping
    public ResponseEntity<ResultDto> updateWishlistUnitCount(@Valid @RequestBody UpdateWishlistRequest updateWishlistRequest) {
        wishlistService.updateWishlist(updateWishlistRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    @DeleteMapping
    public ResponseEntity<ResultDto> deleteWishlist(@Valid @RequestBody DeleteWishlistRequest deleteWishlistRequest) {
        wishlistService.deleteWishlist(deleteWishlistRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
