package com.cerpha.productservice.cerpha.product.controller;

import com.cerpha.productservice.cerpha.product.request.OrderProductListRequest;
import com.cerpha.productservice.cerpha.product.request.RestoreStockRequest;
import com.cerpha.productservice.cerpha.product.response.OrderProductListResponse;
import com.cerpha.productservice.cerpha.product.response.ProductDetailResponse;
import com.cerpha.productservice.cerpha.product.service.ProductService;
import com.cerpha.productservice.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/products")
public class ProductInternalController {

    private final ProductService productService;

    public ProductInternalController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 상품 ID 조회
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ResultDto<ProductDetailResponse>> getProductForWishlist(
            @PathVariable("productId") Long productId) {
        ProductDetailResponse productDetail = productService.getProductForWishlist(productId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, productDetail));
    }

    /**
     * 주문 시 상품 정보 조회
     * @param orderProductListRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<ResultDto<OrderProductListResponse>> getOrderProductsDetail(
            @RequestBody OrderProductListRequest orderProductListRequest) {
        OrderProductListResponse orderProductListResponse = productService.getOrderProductsDetail(orderProductListRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, orderProductListResponse));
    }

    /**
     * 주문 시 재고 감소
     * @param orderProductListRequest
     * @return
     */
    @PostMapping("/order")
    public ResponseEntity<ResultDto> decreaseStock(
            @RequestBody OrderProductListRequest orderProductListRequest) {
        productService.decreaseStock(orderProductListRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    /**
     * 주문 취소 시 재고 복구
     * @param restoreStockRequest
     * @return
     */
    @PostMapping("/cancel")
    public ResponseEntity restoreStock(@RequestBody RestoreStockRequest restoreStockRequest) {
        productService.restoreStock(restoreStockRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

}
