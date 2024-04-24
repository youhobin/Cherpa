package com.cerpha.orderservice.cerpha.order.service;

import com.cerpha.orderservice.cerpha.order.domain.Order;
import com.cerpha.orderservice.cerpha.order.domain.OrderProduct;
import com.cerpha.orderservice.cerpha.order.repository.OrderProductRepository;
import com.cerpha.orderservice.cerpha.order.repository.OrderRepository;
import com.cerpha.orderservice.cerpha.order.request.AddOrderRequest;
import com.cerpha.orderservice.cerpha.order.request.OrderListRequest;
import com.cerpha.orderservice.cerpha.order.response.OrderListResponse;
import com.cerpha.orderservice.cerpha.order.response.OrderProductResponse;
import com.cerpha.orderservice.cerpha.order.response.OrderResponse;
import com.cerpha.orderservice.common.client.product.ProductClient;
import com.cerpha.orderservice.common.client.product.request.CancelOrderProductRequest;
import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.cerpha.orderservice.common.client.product.request.GetProductsNameRequest;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.client.product.response.AddOrderProductResponse;
import com.cerpha.orderservice.common.client.product.response.ProductNameResponse;
import com.cerpha.orderservice.common.client.user.UserClient;
import com.cerpha.orderservice.common.exception.BusinessException;
import com.cerpha.orderservice.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cerpha.orderservice.cerpha.order.domain.OrderStatus.PAYMENT;
import static com.cerpha.orderservice.cerpha.order.domain.OrderStatus.SHIPPING;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final OrderProductRepository orderProductRepository;

    public OrderService(OrderRepository orderRepository, UserClient userClient, ProductClient productClient, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.productClient = productClient;
        this.orderProductRepository = orderProductRepository;
    }

    @Transactional
    public void addOrder(AddOrderRequest request) {
        Long userId = userClient.getUserId(request.getUserId()).getResultData();

        List<AddOrderProductResponse> orderProductResponses =
                productClient.decreaseStock(new DecreaseStockRequest(request.getOrderProducts())).getResultData().getProducts();

        long totalPrice = getTotalPrice(orderProductResponses);

        Order order = Order.builder()
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryPhone(request.getDeliveryPhone())
                .status(PAYMENT)
                .userId(userId)
                .totalPrice(totalPrice)
                .build();

        orderRepository.save(order);

        List<OrderProduct> orderProducts = orderProductResponses.stream()
                .map(op -> OrderProduct.builder()
                        .order(order)
                        .productId(op.getProductId())
                        .unitCount(op.getUnitCount())
                        .orderProductPrice(op.getPrice() * op.getUnitCount())
                        .build())
                .toList();

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
                    List<OrderProduct> orderProductList = entry.getValue();

                    List<Long> productIds = orderProductList.stream()
                            .map(OrderProduct::getProductId)
                            .distinct()
                            .toList();

                    List<ProductNameResponse> productNames =
                            productClient.getProductsName(new GetProductsNameRequest(productIds)).getResultData().getNameResponses();

                    Map<Long, String> productNamesMap = productNames.stream()
                            .collect(Collectors.toMap(ProductNameResponse::getProductId, ProductNameResponse::getName));


                    List<OrderProductResponse> orderProductResponses = orderProductList.stream()
                            .map(op -> OrderProductResponse.builder()
                                    .productId(op.getProductId())
                                    .productName(productNamesMap.get(op.getProductId()))
                                    .unitCount(op.getUnitCount())
                                    .build())
                            .toList();

                    return OrderResponse.builder()
                            .userId(order.getUserId())
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

        List<CancelOrderProductRequest> cancelOrderProductRequests = orderProducts.stream()
                .map(op -> new CancelOrderProductRequest(op.getProductId(), op.getUnitCount()))
                .toList();

        RestoreStockRequest restoreStockRequest = new RestoreStockRequest(cancelOrderProductRequests);
        productClient.restoreStock(restoreStockRequest);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_ORDER));
        order.cancel();
    }

    @Scheduled(cron = "${env.order.changeStatusCycle}")
    @Transactional
    public void updateOrderStatus() {
        log.info("Update OrderStatus Scheduling");
        LocalDate yesterday = LocalDate.now().minusDays(1);

        LocalDateTime start = yesterday.atStartOfDay();
        LocalDateTime end = yesterday.atTime(LocalTime.MAX);

        List<Order> shippingOrders = orderRepository.findOrdersByStatusAndUpdatedAtBetween(SHIPPING, start, end);
        log.info("shippingOrders={}", shippingOrders.size());

        shippingOrders.forEach(Order::finishDelivery);

        List<Order> paymentOrders = orderRepository.findOrdersByStatusAndUpdatedAtBetween(PAYMENT, start, end);
        log.info("paymentOrders={}", paymentOrders.size());

        paymentOrders.forEach(Order::startDelivery);
    }

    private List<OrderResponse> sortOrderResponse(List<OrderResponse> orderResponses) {
        return orderResponses.stream()
                .sorted(Comparator.comparing(OrderResponse::getUpdatedAt).reversed())
                .toList();
    }

    private static long getTotalPrice(List<AddOrderProductResponse> orderProductResponses) {
        return orderProductResponses.stream()
                .mapToLong(op -> op.getPrice() * op.getUnitCount())
                .sum();
    }

}
