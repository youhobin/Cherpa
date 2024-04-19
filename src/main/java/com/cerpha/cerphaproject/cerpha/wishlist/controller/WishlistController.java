package com.cerpha.cerphaproject.cerpha.wishlist.controller;

import com.cerpha.cerphaproject.cerpha.wishlist.request.AddWishlistRequest;
import com.cerpha.cerphaproject.cerpha.wishlist.service.WishlistService;
import com.cerpha.cerphaproject.common.dto.ResultDto;
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
                                      @RequestBody AddWishlistRequest addWishlistRequest) {
        wishlistService.addWishlist(userId, addWishlistRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
