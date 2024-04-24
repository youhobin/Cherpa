package com.cerpha.cerphaproject.cerpha.product.controller;

import com.cerpha.cerphaproject.cerpha.product.response.ProductListResponse;
import com.cerpha.cerphaproject.cerpha.product.response.ProductResponse;
import com.cerpha.cerphaproject.cerpha.product.service.ProductService;
import com.cerpha.cerphaproject.common.dto.PageResponseDto;
import com.cerpha.cerphaproject.common.dto.ResultDto;
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

    @GetMapping
    public ResponseEntity<ResultDto<PageResponseDto<ProductListResponse>>> getAllProducts(
            @RequestParam int page,
            @RequestParam int size) {
        PageResponseDto<ProductListResponse> pagingResponse = productService.getAllProducts(page, size);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, pagingResponse));
    }

    @GetMapping("/{productId}")
    public ResponseEntity getProductDetail(@PathVariable("productId") Long productId) {
        ProductResponse productDetail = productService.getProductDetail(productId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, productDetail));
    }
}
