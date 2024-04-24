package com.cerpha.productservice.cerpha.product.controller;

import com.cerpha.productservice.cerpha.product.request.DecreaseStockRequest;
import com.cerpha.productservice.cerpha.product.request.GetProductsNameRequest;
import com.cerpha.productservice.cerpha.product.request.WishlistProductsRequest;
import com.cerpha.productservice.cerpha.product.response.*;
import com.cerpha.productservice.cerpha.product.service.ProductService;
import com.cerpha.productservice.common.dto.PageResponseDto;
import com.cerpha.productservice.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ResultDto> getProductDetail(@PathVariable("productId") Long productId) {
        ProductResponse productDetail = productService.getProductDetail(productId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, productDetail));
    }

}
