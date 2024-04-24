package com.cerpha.orderservice.common.client.product;

import com.cerpha.orderservice.cerpha.wishlist.response.WishlistResponse;
import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.cerpha.orderservice.common.client.product.request.GetProductsNameRequest;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.client.product.request.WishlistProductRequest;
import com.cerpha.orderservice.common.client.product.response.OrderProductListResponse;
import com.cerpha.orderservice.common.client.product.response.ProductNameListResponse;
import com.cerpha.orderservice.common.dto.ResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/internal/products/{productId}")
    ResultDto<Long> getProductId(@PathVariable("productId") Long productId);

    @PostMapping("/api/internal/products/wishlist")
    ResultDto<List<WishlistResponse>> getProductsInWishList(@RequestBody WishlistProductRequest wishlistProductRequest);

    @PostMapping("/api/internal/products/order")
    ResultDto<OrderProductListResponse> decreaseStock(@RequestBody DecreaseStockRequest decreaseStockRequest);

    @PostMapping("/api/internal/products/names")
    ResultDto<ProductNameListResponse> getProductsName(@RequestBody GetProductsNameRequest getProductsNameRequest);

    @PostMapping("/api/internal/products/cancel")
    ResultDto restoreStock(@RequestBody RestoreStockRequest restoreStockRequest);
}
