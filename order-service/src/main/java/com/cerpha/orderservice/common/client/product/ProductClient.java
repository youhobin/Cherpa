package com.cerpha.orderservice.common.client.product;

import com.cerpha.orderservice.cerpha.order.request.AddOrderProductRequest;
import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.cerpha.orderservice.common.client.product.request.OrderProductListRequest;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.client.product.response.OrderProductListResponse;
import com.cerpha.orderservice.common.client.product.response.ProductDetailResponse;
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
    ResultDto<ProductDetailResponse> getProductForWishlist(@PathVariable("productId") Long productId);

    @PostMapping("/api/internal/products")
    ResultDto<OrderProductListResponse> getOrderProductsDetail(@RequestBody OrderProductListRequest orderProductListRequest);

//    @PostMapping("/api/internal/products/order")
//    ResultDto decreaseStock(@RequestBody OrderProductListRequest orderProductListRequest);

    @PostMapping("/api/internal/products/order")
    ResultDto decreaseStock(@RequestBody DecreaseStockRequest decreaseStockRequest);

    @PostMapping("/api/internal/products/cancel")
    ResultDto restoreStock(@RequestBody RestoreStockRequest restoreStockRequest);
}
