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
import com.cerpha.orderservice.cerpha.wishlist.service.WishlistService;
import com.cerpha.orderservice.common.client.product.ProductClient;
import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.cerpha.orderservice.common.client.product.request.ProductUnitCountRequest;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.client.product.response.AddOrderProductResponse;
import com.cerpha.orderservice.common.client.user.UserClient;
import com.cerpha.orderservice.common.exception.BusinessException;
import com.cerpha.orderservice.common.exception.ExceptionCode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static com.cerpha.orderservice.cerpha.order.domain.OrderStatus.PAYMENT;
import static com.cerpha.orderservice.cerpha.order.domain.OrderStatus.SHIPPING;
import static com.cerpha.orderservice.common.exception.ExceptionCode.NOT_AVAILABLE_CANCEL;
import static com.cerpha.orderservice.common.exception.ExceptionCode.NOT_AVAILABLE_ORDER;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final OrderProductRepository orderProductRepository;
    private final WishlistService wishlistService;

    public OrderService(OrderRepository orderRepository, UserClient userClient, ProductClient productClient, OrderProductRepository orderProductRepository, WishlistService wishlistService) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.productClient = productClient;
        this.orderProductRepository = orderProductRepository;
        this.wishlistService = wishlistService;
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "addOrderFallback")
    @Retry(name = "product-service")
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
                        .productName(op.getProductName())
                        .build())
                .toList();

        orderProductRepository.saveAll(orderProducts);

        wishlistService.deleteAllWishList(userId);
    }

    public void addOrderFallback(AddOrderRequest request, Throwable e) {
        log.error(e.getMessage());
        throw new BusinessException(NOT_AVAILABLE_ORDER);
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

                    List<OrderProductResponse> orderProductResponses = orderProductList.stream()
                            .map(op -> OrderProductResponse.builder()
                                    .productId(op.getProductId())
                                    .productName(op.getProductName())
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

    @CircuitBreaker(name = "product-service", fallbackMethod = "cancelOrderFallback")
    @Retry(name = "product-service")
    @Transactional
    public void cancelOrder(Long orderId) {
        List<OrderProduct> orderProducts = orderProductRepository.findOrderProductsByOrderId(orderId);

        List<ProductUnitCountRequest> productUnitCountRequests = orderProducts.stream()
                .map(op -> new ProductUnitCountRequest(op.getProductId(), op.getUnitCount()))
                .toList();

        RestoreStockRequest restoreStockRequest = new RestoreStockRequest(productUnitCountRequests);
        productClient.restoreStock(restoreStockRequest);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_ORDER));
        order.cancel();
    }

    public void cancelOrderFallback(Long orderId, Throwable e) {
        log.error(e.getMessage());
        throw new BusinessException(NOT_AVAILABLE_CANCEL);
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
