package com.cerpha.cerphaproject.cerpha.order.service;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import com.cerpha.cerphaproject.cerpha.order.domain.OrderProduct;
import com.cerpha.cerphaproject.cerpha.order.domain.OrderStatus;
import com.cerpha.cerphaproject.cerpha.order.repository.OrderRepository;
import com.cerpha.cerphaproject.cerpha.order.request.AddOrderRequest;
import com.cerpha.cerphaproject.cerpha.product.repository.ProductRepository;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.user.repository.UserRepository;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void addOrder(AddOrderRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_USER));

        List<OrderProduct> orderProducts = request.getOrderProducts().stream()
                .map(p -> OrderProduct.addOrderProduct(productRepository.findById(p.getProductId())
                        .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_PRODUCT)), p.getUnitCount()))
                .toList();

        Order order = Order.addOrder(request.getDeliveryAddress(), request.getDeliveryPhone(), user, orderProducts);

        orderRepository.save(order);
    }

}
