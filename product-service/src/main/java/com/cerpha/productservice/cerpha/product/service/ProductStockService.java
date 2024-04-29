package com.cerpha.productservice.cerpha.product.service;

import com.cerpha.productservice.cerpha.product.domain.Product;
import com.cerpha.productservice.cerpha.product.repository.ProductRepository;
import com.cerpha.productservice.cerpha.product.request.AddOrderProductRequest;
import com.cerpha.productservice.common.exception.BusinessException;
import com.cerpha.productservice.common.exception.ExceptionCode;
import com.cerpha.productservice.common.redis.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductStockService {

    private final ProductRepository productRepository;

    public ProductStockService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @DistributedLock(key = "#request.getProductId()")
    public void decreaseStock(AddOrderProductRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_PRODUCT));

        product.decreaseStock(request.getUnitCount());
    }
}