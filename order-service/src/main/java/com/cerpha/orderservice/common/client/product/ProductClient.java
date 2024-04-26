package com.cerpha.orderservice.common.client.product;

import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.client.product.response.OrderProductListResponse;
import com.cerpha.orderservice.common.client.product.response.ProductDetailResponse;
import com.cerpha.orderservice.common.dto.ResultDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/internal/products/{productId}")
    ResultDto<ProductDetailResponse> getProductForWishlist(@PathVariable("productId") Long productId);

    @PostMapping("/api/internal/products/order")
    ResultDto<OrderProductListResponse> decreaseStock(@RequestBody DecreaseStockRequest decreaseStockRequest);

    @PostMapping("/api/internal/products/cancel")
    ResultDto restoreStock(@RequestBody RestoreStockRequest restoreStockRequest);


    @GetMapping("/errorful/case1")
    String case1();
    @GetMapping("/errorful/case2")
    String case2();

    @GetMapping("/errorful/case3")
    String case3();

}
