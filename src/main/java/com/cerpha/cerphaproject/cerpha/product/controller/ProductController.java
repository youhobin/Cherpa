package com.cerpha.cerphaproject.cerpha.product.controller;

import com.cerpha.cerphaproject.cerpha.product.domain.Products;
import com.cerpha.cerphaproject.cerpha.product.response.ProductListResponse;
import com.cerpha.cerphaproject.cerpha.product.service.ProductService;
import com.cerpha.cerphaproject.common.dto.PageResponseDto;
import com.cerpha.cerphaproject.common.dto.ResultDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
