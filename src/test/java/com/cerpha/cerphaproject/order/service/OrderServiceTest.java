package com.cerpha.cerphaproject.order.service;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import com.cerpha.cerphaproject.cerpha.order.repository.OrderProductRepository;
import com.cerpha.cerphaproject.cerpha.order.repository.OrderRepository;
import com.cerpha.cerphaproject.cerpha.order.request.AddOrderProductRequest;
import com.cerpha.cerphaproject.cerpha.order.request.AddOrderRequest;
import com.cerpha.cerphaproject.cerpha.order.service.OrderService;
import com.cerpha.cerphaproject.cerpha.product.domain.Product;
import com.cerpha.cerphaproject.cerpha.product.repository.ProductRepository;
import com.cerpha.cerphaproject.cerpha.user.domain.UserRole;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    public void order() throws InterruptedException {
        // given
        int numThreads = 10;
        CountDownLatch doneSignal = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        Users user = Users.builder()
                .email("ghdb132@naver.com")
                .password("132465aa")
                .name("hobin")
                .nickname("ghgh")
                .phone("01012345678")
                .address("인천시")
                .role(UserRole.USER)
                .build();

        Users savedUser = userRepository.save(user);
        Product product = new Product(1L, "신발", "신발입니다.", 10000L, 10L, "hobin");
        Product savedProduct = productRepository.save(product);

        List<AddOrderProductRequest> lists = new ArrayList<>();
        AddOrderProductRequest addOrderProductRequest = new AddOrderProductRequest(savedProduct.getId(), 2L);
        lists.add(addOrderProductRequest);

        AddOrderRequest addOrderRequest = AddOrderRequest.builder()
                .userId(savedUser.getId())
                .deliveryPhone(savedUser.getPhone())
                .deliveryAddress(savedUser.getAddress())
                .orderProducts(lists)
                .build();

        // when
        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    orderService.addOrder(addOrderRequest);
                    System.out.println("Qwer" + productRepository.findById(1L).get().getStock());
                    successCount.getAndIncrement();
                    System.out.println("성공");
                } catch (Exception e) {
                    failCount.getAndIncrement();
                    System.out.println("실패");
                } finally {
                    doneSignal.countDown();
                }
            });
        }

        doneSignal.await();
        executorService.shutdown();

        // then
        Assertions.assertThat(successCount.get()).isEqualTo(5);
        Assertions.assertThat(failCount.get()).isEqualTo(5);
    }
}
