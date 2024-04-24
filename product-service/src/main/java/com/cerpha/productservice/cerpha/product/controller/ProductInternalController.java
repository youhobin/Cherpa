package com.cerpha.productservice.cerpha.product.controller;

import com.cerpha.productservice.cerpha.product.request.DecreaseStockRequest;
import com.cerpha.productservice.cerpha.product.request.GetProductsNameRequest;
import com.cerpha.productservice.cerpha.product.request.RestoreStockRequest;
import com.cerpha.productservice.cerpha.product.request.WishlistProductsRequest;
import com.cerpha.productservice.cerpha.product.response.OrderProductListResponse;
import com.cerpha.productservice.cerpha.product.response.ProductNameListResponse;
import com.cerpha.productservice.cerpha.product.response.WishlistResponse;
import com.cerpha.productservice.cerpha.product.service.ProductService;
import com.cerpha.productservice.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal/products")
public class ProductInternalController {

    private final ProductService productService;

    public ProductInternalController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResultDto<Long>> getProductId(@PathVariable("productId") Long productId) {
        Long savedProductId = productService.getProductId(productId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, savedProductId));
    }

    @PostMapping("/wishlist")
    public ResponseEntity<ResultDto<List<WishlistResponse>>> getProductsInWishList(@RequestBody WishlistProductsRequest wishListProductsRequest) {
        List<WishlistResponse> productsResponse = productService.getProductsInWishList(wishListProductsRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, productsResponse));
    }

    @PostMapping("/order")
    public ResponseEntity<ResultDto<OrderProductListResponse>> decreaseStock(@RequestBody DecreaseStockRequest decreaseStockRequest) {
        OrderProductListResponse orderProductListResponse = productService.decreaseStock(decreaseStockRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, orderProductListResponse));
    }

    @PostMapping("/names")
    public ResponseEntity<ResultDto<ProductNameListResponse>> getProductsName(@RequestBody GetProductsNameRequest getProductsNameRequest) {
        ProductNameListResponse productsName = productService.getProductsName(getProductsNameRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, productsName));
    }

    @PostMapping("/cancel")
    public ResponseEntity restoreStock(@RequestBody RestoreStockRequest restoreStockRequest) {
        productService.restoreStock(restoreStockRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
