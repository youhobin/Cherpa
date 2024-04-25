package com.cerpha.productservice.cerpha.product.controller;

import com.cerpha.productservice.cerpha.product.request.DecreaseStockRequest;
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
    public ResponseEntity<ResultDto<ProductDetailResponse>> getProductForWishlist(@PathVariable("productId") Long productId) {
        ProductDetailResponse productDetail = productService.getProductForWishlist(productId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, productDetail));
    }

    /**
     * 주문 시 재고 감소
     * @param decreaseStockRequest
     * @return
     */
    @PostMapping("/order")
    public ResponseEntity<ResultDto<OrderProductListResponse>> decreaseStock(@RequestBody DecreaseStockRequest decreaseStockRequest) {
        OrderProductListResponse orderProductListResponse = productService.decreaseStock(decreaseStockRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, orderProductListResponse));
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
