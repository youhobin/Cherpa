package com.cerpha.cerphaproject.cerpha.order.service;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import com.cerpha.cerphaproject.cerpha.order.domain.OrderProduct;
import com.cerpha.cerphaproject.cerpha.order.repository.OrderProductRepository;
import com.cerpha.cerphaproject.cerpha.order.repository.OrderRepository;
import com.cerpha.cerphaproject.cerpha.order.request.AddOrderRequest;
import com.cerpha.cerphaproject.cerpha.order.response.OrderListResponse;
import com.cerpha.cerphaproject.cerpha.order.response.OrderProductResponse;
import com.cerpha.cerphaproject.cerpha.order.response.OrderResponse;
import com.cerpha.cerphaproject.cerpha.product.repository.ProductRepository;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.user.repository.UserRepository;
import com.cerpha.cerphaproject.cerpha.user.request.OrderListRequest;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.cerpha.cerphaproject.cerpha.order.domain.OrderStatus.PAYMENT;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Transactional
    public void addOrder(AddOrderRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_USER));

        List<OrderProduct> orderProducts = request.getOrderProducts().stream()
                .map(p -> OrderProduct.addOrderProduct(productRepository.findById(p.getProductId())
                        .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_PRODUCT)), p.getUnitCount()))
                .toList();

        Order order = Order.builder()
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryPhone(request.getDeliveryPhone())
                .status(PAYMENT)
                .user(user)
                .totalPrice(getTotalOrderPrice(orderProducts))
                .build();

        orderRepository.save(order);

        orderProducts.forEach(orderProduct -> orderProduct.addOrder(order));

        orderProductRepository.saveAll(orderProducts);
    }

    @Transactional(readOnly = true)
    public OrderListResponse getOrderList(OrderListRequest request) {
        List<OrderProduct> orderProducts =
                orderProductRepository.findOrderProductsByUserId(request.getUserId());

        List<OrderResponse> orderResponses = orderProducts.stream()
                .collect(groupingBy(OrderProduct::getOrder))
                .entrySet().stream()
                .map(entry -> {
                    Order order = entry.getKey();
                    List<OrderProduct> orderProduct = entry.getValue();

                    List<OrderProductResponse> orderProductResponses = orderProduct.stream()
                            .map(op -> OrderProductResponse.builder()
                                    .productId(op.getProduct().getId())
                                    .productName(op.getProduct().getName())
                                    .unitCount(op.getUnitCount())
                                    .build())
                            .toList();

                    return OrderResponse.builder()
                            .userId(order.getUser().getId())
                            .orderId(order.getId())
                            .deliveryAddress(order.getDeliveryAddress())
                            .deliveryPhone(order.getDeliveryPhone())
                            .totalPrice(order.getTotalPrice())
                            .status(order.getStatus().toString())
                            .orderProducts(orderProductResponses)
                            .updatedAt(order.getUpdatedAt())
                            .build();
                })
                .toList();

        List<OrderResponse> sortedResponses = sortOrderResponse(orderResponses);

        return new OrderListResponse(sortedResponses);

    }

    @Transactional
    public void cancelOrder(Long orderId) {
        List<OrderProduct> orderProducts = orderProductRepository.findOrderProductsByOrderId(orderId);

        orderProducts
                .forEach(orderProduct -> orderProduct.getProduct().restoreStock(orderProduct.getUnitCount()));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_ORDER));
        order.cancel();
    }

    private List<OrderResponse> sortOrderResponse(List<OrderResponse> orderResponses) {
        return orderResponses.stream()
                .sorted(Comparator.comparing(OrderResponse::getUpdatedAt).reversed())
                .toList();
    }

    private static long getTotalOrderPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToLong(p -> p.getUnitPrice() * p.getUnitCount())
                .sum();
    }
}
