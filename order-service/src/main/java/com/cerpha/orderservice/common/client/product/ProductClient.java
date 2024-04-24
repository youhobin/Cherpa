package com.cerpha.orderservice.common.client.product;

import com.cerpha.orderservice.cerpha.wishlist.response.WishlistResponse;
import com.cerpha.orderservice.common.client.product.request.WishlistProductRequest;
import com.cerpha.orderservice.common.client.product.response.ProductResponse;
import com.cerpha.orderservice.common.dto.ResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{productId}")
    ResultDto<ProductResponse> getProductDetail(@PathVariable("productId") Long productId);

    @PostMapping("/api/products/wishlist")
    ResultDto<List<WishlistResponse>> getProductsInWishList(@RequestBody WishlistProductRequest wishlistProductRequest);
}
