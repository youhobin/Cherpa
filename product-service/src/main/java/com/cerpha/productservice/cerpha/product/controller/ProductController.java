package com.cerpha.productservice.cerpha.product.controller;

import com.cerpha.productservice.cerpha.product.response.ProductListResponse;
import com.cerpha.productservice.cerpha.product.response.ProductResponse;
import com.cerpha.productservice.cerpha.product.response.ProductStockResponse;
import com.cerpha.productservice.cerpha.product.service.ProductService;
import com.cerpha.productservice.common.dto.PageResponseDto;
import com.cerpha.productservice.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 상품 전체 조회
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public ResponseEntity<ResultDto<PageResponseDto<ProductListResponse>>> getAllProducts(
            @RequestParam int page,
            @RequestParam int size) {
        PageResponseDto<ProductListResponse> pagingResponse = productService.getAllProducts(page, size);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, pagingResponse));
    }

    /**
     * 단일 상품 상세 조회
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ResultDto<ProductResponse>> getProductDetail(@PathVariable("productId") Long productId) {
        ProductResponse productDetail = productService.getProductDetail(productId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, productDetail));
    }

    @GetMapping("/{productId}/stock")
    public ResponseEntity<ResultDto<ProductStockResponse>> getProductStock(@PathVariable("productId") Long productId) {
        ProductStockResponse productStock = productService.getProductStock(productId);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, productStock));
    }

}
