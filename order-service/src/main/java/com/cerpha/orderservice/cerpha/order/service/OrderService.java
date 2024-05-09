package com.cerpha.orderservice.cerpha.order.service;

import com.cerpha.orderservice.cerpha.order.domain.Order;
import com.cerpha.orderservice.cerpha.order.domain.OrderProduct;
import com.cerpha.orderservice.cerpha.order.repository.OrderProductRepository;
import com.cerpha.orderservice.cerpha.order.repository.OrderRepository;
import com.cerpha.orderservice.cerpha.order.request.*;
import com.cerpha.orderservice.cerpha.order.response.OrderListResponse;
import com.cerpha.orderservice.cerpha.order.response.OrderResponse;
import com.cerpha.orderservice.cerpha.wishlist.service.WishlistService;
import com.cerpha.orderservice.common.client.exception.FeignClientException;
import com.cerpha.orderservice.common.client.product.ProductClient;
import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.cerpha.orderservice.common.client.product.request.OrderProductListRequest;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.client.product.response.OrderProductDetailResponse;
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

import static com.cerpha.orderservice.cerpha.order.domain.OrderStatus.*;
import static com.cerpha.orderservice.common.exception.ExceptionCode.*;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderProductRepository orderProductRepository;
    private final WishlistService wishlistService;
    private final OrderProducer orderProducer;

    public OrderService(OrderRepository orderRepository, ProductClient productClient, OrderProductRepository orderProductRepository, WishlistService wishlistService, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.orderProductRepository = orderProductRepository;
        this.wishlistService = wishlistService;
        this.orderProducer = orderProducer;
    }

    /**
     * 주문 생성 시 결제 진입
     *
     * @param request
     */
    @Transactional
    public void addOrderWithPayment(AddOrderRequest request, Long userId) {
        Order order = Order.builder()
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryPhone(request.getDeliveryPhone())
                .status(PAYMENT_WAITING)
                .userId(userId)
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderProduct> orderProducts = request.getOrderProducts().stream()
                .map(op -> OrderProduct.builder()
                        .order(order)
                        .productId(op.getProductId())
                        .unitCount(op.getUnitCount())
                        .build()) 
                .toList();

        orderProductRepository.saveAll(orderProducts);

        wishlistService.deleteAllWishList(userId);

        // 재고 감소 kafka
        orderProducer.decreaseStock(new DecreaseStockRequest(userId, savedOrder.getId(), request.getOrderProducts()));

        // 결제
        orderProducer.processPayment(new ProcessPaymentRequest(userId, savedOrder.getId()));
    }

    /**
     * 주문 생성 시 결제 진입
     *
     * @param request
     */
//    @CircuitBreaker(name = "product-service", fallbackMethod = "addOrderWithPaymentFallback")
//    @Retry(name = "product-service")
//    @Transactional
//    public void addOrderWithPayment(AddOrderRequest request, Long userId) {
//        Order order = Order.builder()
//                .deliveryAddress(request.getDeliveryAddress())
//                .deliveryPhone(request.getDeliveryPhone())
//                .status(PAYMENT_WAITING)
//                .userId(userId)
//                .build();
//
//        Order savedOrder = orderRepository.save(order);
//
//        List<OrderProduct> orderProducts = request.getOrderProducts().stream()
//                .map(op -> OrderProduct.builder()
//                        .order(order)
//                        .productId(op.getProductId())
//                        .unitCount(op.getUnitCount())
//                        .build())
//                .toList();
//
//        orderProductRepository.saveAll(orderProducts);
//
//        wishlistService.deleteAllWishList(userId);
//
//        // 재고 감소
//        productClient.decreaseStock(new DecreaseStockRequest(userId, savedOrder.getId(),request.getOrderProducts()));
//    }

//    public void addOrderWithPaymentFallback(AddOrderRequest request, Long userId, FeignClientException e) {
//        productClient.restoreStock(new RestoreStockRequest(request.getOrderProducts()));
//        throw new FeignClientException(e.getExceptionResponse());
//    }
    public void addOrderWithPaymentFallback(AddOrderRequest request, Long userId, BusinessException e) {
        throw new BusinessException(e.getExceptionCode());
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

                    List<ProductUnitCountRequest> productUnitCountRequests = orderProductList.stream()
                            .map(op -> new ProductUnitCountRequest(op.getProductId(), op.getUnitCount()))
                            .toList();

                    List<OrderProductDetailResponse> orderProductResponses =
                            productClient.getOrderProductsDetail(new OrderProductListRequest(productUnitCountRequests)).getResultData().getProducts();

                    long totalPrice = getTotalPrice(orderProductResponses);

                    return OrderResponse.builder()
                            .userId(order.getUserId())
                            .orderId(order.getId())
                            .deliveryAddress(order.getDeliveryAddress())
                            .deliveryPhone(order.getDeliveryPhone())
                            .totalPrice(totalPrice)
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

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_ORDER));
        order.cancel();

        RestoreStockRequest restoreStockRequest = new RestoreStockRequest(productUnitCountRequests);
        productClient.restoreStock(restoreStockRequest);
    }

    public void cancelOrderFallback(Long orderId, BusinessException e) {
        throw new BusinessException(NOT_AVAILABLE_CANCEL);
    }

    @Transactional
    public void completeOrderPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_ORDER));

        order.completeOrderPayment();
    }

    @Transactional
    public void rollbackCreatedOrder(Long orderId) {
        orderProductRepository.deleteAllByOrderId(orderId);
        orderRepository.deleteById(orderId);
//        Long orderId = orderRollbackRequest.getOrderId();
//        if (orderRollbackRequest.isFullRollback()) {
//            List<OrderProduct> orderProducts = orderProductRepository.findOrderProductsByOrderId(orderId);
//
//            List<ProductUnitCountRequest> productUnitCountRequests = orderProducts.stream()
//                    .map(op -> new ProductUnitCountRequest(op.getProductId(), op.getUnitCount()))
//                    .toList();
//            RestoreStockRequest restoreStockRequest = new RestoreStockRequest(productUnitCountRequests);
//            orderProducer.restoreStock(restoreStockRequest);
//        }
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new BusinessException(ExceptionCode.NOT_FOUND_ORDER));
//        order.cancel();
    }

    @Transactional
    public void cancelCreatedOrder(Long orderId) {
        List<OrderProduct> orderProducts = orderProductRepository.findOrderProductsByOrderId(orderId);

        List<ProductUnitCountRequest> productUnitCountRequests = orderProducts.stream()
                .map(op -> new ProductUnitCountRequest(op.getProductId(), op.getUnitCount()))
                .toList();
        RestoreStockRequest restoreStockRequest = new RestoreStockRequest(productUnitCountRequests);
        orderProducer.restoreStock(restoreStockRequest);

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

    private static long getTotalPrice(List<OrderProductDetailResponse> orderProductResponses) {
        return orderProductResponses.stream()
                .mapToLong(op -> op.getPrice() * op.getUnitCount())
                .sum();
    }
}
