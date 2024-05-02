package com.cerpha.productservice.cerpha.product.service;

import com.cerpha.productservice.cerpha.product.domain.Product;
import com.cerpha.productservice.cerpha.product.repository.ProductRepository;
import com.cerpha.productservice.cerpha.product.request.AddOrderProductRequest;
import com.cerpha.productservice.cerpha.product.request.OrderProductListRequest;
import com.cerpha.productservice.common.exception.BusinessException;
import com.cerpha.productservice.common.exception.ExceptionCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("재고 감소 분산락 테스트")
    @Test
    public void decreaseProductsStock() throws InterruptedException {
        // given
        int numThreads = 10;
        CountDownLatch doneSignal = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        Product product = new Product(1L, "신발", "신발입니다.", 10000L, 10L, "hobin");
        productRepository.save(product);

        List<AddOrderProductRequest> list = new ArrayList<>();
        list.add(new AddOrderProductRequest(product.getId(), 2L));

        OrderProductListRequest orderProductListRequest = new OrderProductListRequest(list);

        // when
        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
//                    productService.decreaseProductsStock(orderProductListRequest);
                    successCount.getAndIncrement();
                } catch (BusinessException e) {
                    failCount.getAndIncrement();
                } finally {
                    doneSignal.countDown();
                }
            });
        }

        doneSignal.await();
        executorService.shutdown();

        Product savedProduct =
                productRepository.findById(1L)
                        .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_PRODUCT));
        Assertions.assertThat(savedProduct.getStock()).isEqualTo(0);
        Assertions.assertThat(successCount.get()).isEqualTo(5);
        Assertions.assertThat(failCount.get()).isEqualTo(5);
    }

}