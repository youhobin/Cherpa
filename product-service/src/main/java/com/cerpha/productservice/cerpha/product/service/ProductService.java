package com.cerpha.productservice.cerpha.product.service;

import com.cerpha.productservice.cerpha.product.domain.Product;
import com.cerpha.productservice.cerpha.product.repository.ProductRepository;
import com.cerpha.productservice.cerpha.product.request.*;
import com.cerpha.productservice.cerpha.product.response.*;
import com.cerpha.productservice.common.dto.PageResponseDto;
import com.cerpha.productservice.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cerpha.productservice.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;

    public ProductService(ProductRepository productRepository, RedissonClient redissonClient) {
        this.productRepository = productRepository;
        this.redissonClient = redissonClient;
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
                .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .producer(product.getProducer())
                .build();
    }

    @Transactional
    public OrderProductListResponse getOrderProductsDetail(OrderProductListRequest request) {
        List<AddOrderProductResponse> orderProductResponses = request.getOrderProducts().stream()
                .map(op -> {
                    Product product = productRepository.findById(op.getProductId())
                            .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

                    return AddOrderProductResponse.builder()
                            .productId(product.getId())
                            .unitCount(op.getUnitCount())
                            .price(product.getPrice())
                            .productName(product.getName())
                            .build();
                })
                .toList();
        return new OrderProductListResponse(orderProductResponses);
    }

    @Transactional
    public void decreaseProductsStock(OrderProductListRequest request) {
        String lockKey = "product_1";
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean available = lock.tryLock(10L, 5L, TimeUnit.SECONDS);
            request.getOrderProducts()
                    .forEach(op -> {
                        if (!available) {
                            log.error("lock 획득 실패");
                            throw new BusinessException(LOCK_NOT_AVAILABLE);
                        }

                        Product product = productRepository.findById(op.getProductId())
                                .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

                        log.info("current_stock={}",product.getStock());
                        product.decreaseStock(op.getUnitCount());
                    });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }

//    @Transactional
//    public void decreaseProductsStock(OrderProductListRequest request) {
//        request.getOrderProducts()
//                .forEach(op -> {
//                    String lockKey = "product_" + op.getProductId();
//                    RLock lock = redissonClient.getLock(lockKey);
//
//                    try {
//                        boolean available = lock.tryLock(10L, 5L, TimeUnit.SECONDS);
//
//                        if (available) {
//                            try {
//                                Product product = productRepository.findById(op.getProductId())
//                                        .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));
//
//                                log.info("current_stock={}",product.getStock());
//                                product.decreaseStock(op.getUnitCount());
//                            } finally {
//                                lock.unlock();
//                            }
//
//                        } else {
//                            log.error("lock 획득 실패");
//                            throw new BusinessException(LOCK_NOT_AVAILABLE);
//                        }
//                    } catch (InterruptedException e) {
//                        log.error(e.getMessage());
//                        throw new RuntimeException(e);
//                    }
//                });
//    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductForWishlist(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

        return ProductDetailResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .build();
    }

    @Transactional
    public void restoreStock(RestoreStockRequest request) {
        request.getOrderProducts()
                .forEach(op -> {
                    Product product = productRepository.findById(op.getProductId())
                            .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

                    product.restoreStock(op.getUnitCount());
                });
    }
}
