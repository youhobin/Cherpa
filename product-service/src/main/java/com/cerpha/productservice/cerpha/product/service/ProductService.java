package com.cerpha.productservice.cerpha.product.service;

import com.cerpha.productservice.cerpha.product.domain.Product;
import com.cerpha.productservice.cerpha.product.repository.ProductRepository;
import com.cerpha.productservice.cerpha.product.request.*;
import com.cerpha.productservice.cerpha.product.response.*;
import com.cerpha.productservice.common.dto.PageResponseDto;
import com.cerpha.productservice.common.exception.BusinessException;
import com.cerpha.productservice.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ProductListResponse> getAllProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Product> productsPage = productRepository.findAll(pageRequest);
        List<ProductListResponse> products = productsPage.stream()
                .map(p -> ProductListResponse.builder()
                        .id(p.getId())
                        .producer(p.getProducer())
                        .price(p.getPrice())
                        .name(p.getName())
                        .build())
                .toList();

        return new PageResponseDto<ProductListResponse>(
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast(),
                products);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_PRODUCT));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .producer(product.getProducer())
                .build();
    }

    @Transactional(readOnly = true)
    public List<WishlistResponse> getProductsInWishList(WishlistProductsRequest request) {
        List<ProductRequest> productRequests = request.getProducts();

        return productRequests.stream()
                .map(p -> {
                    Product product = productRepository.findById(p.getProductId())
                            .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_PRODUCT));

                    return WishlistResponse.builder()
                            .wishlistId(p.getWishlistId())
                            .productId(product.getId())
                            .unitCount(p.getUnitCount())
                            .productName(product.getName())
                            .productPrice(product.getPrice())
                            .build();
                })
                .toList();
    }

    @Transactional
    public OrderProductListResponse decreaseStock(DecreaseStockRequest request) {
        List<AddOrderProductResponse> orderProductResponses = request.getOrderProducts().stream()
                .map(op -> {
                    Product product = productRepository.findById(op.getProductId())
                            .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_PRODUCT));

                    product.decreaseStock(op.getUnitCount());

                    return AddOrderProductResponse.builder()
                            .productId(product.getId())
                            .unitCount(op.getUnitCount())
                            .price(product.getPrice())
                            .build();
                })
                .toList();

        return new OrderProductListResponse(orderProductResponses);

    }

    @Transactional(readOnly = true)
    public ProductNameListResponse getProductsName(GetProductsNameRequest request) {
        List<Product> products = productRepository.findByIdIn(request.getProductIds());

        List<ProductNameResponse> productNameResponses = products.stream()
                .map(p -> new ProductNameResponse(p.getId(), p.getName()))
                .toList();

        return new ProductNameListResponse(productNameResponses);
    }

    @Transactional(readOnly = true)
    public Long getProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_PRODUCT));

        return product.getId();
    }

    @Transactional
    public void restoreStock(RestoreStockRequest request) {
        request.getOrderProducts()
                .forEach(op -> {
                    Product product = productRepository.findById(op.getProductId())
                            .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_PRODUCT));

                    product.restoreStock(op.getUnitCount());
                });
    }
}