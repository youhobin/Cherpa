package com.cerpha.orderservice.cerpha.refund.service;

import com.cerpha.orderservice.cerpha.order.domain.Order;
import com.cerpha.orderservice.cerpha.order.domain.OrderProduct;
import com.cerpha.orderservice.cerpha.order.repository.OrderProductRepository;
import com.cerpha.orderservice.cerpha.order.repository.OrderRepository;
import com.cerpha.orderservice.cerpha.order.request.ProductUnitCountRequest;
import com.cerpha.orderservice.cerpha.refund.domain.Refund;
import com.cerpha.orderservice.cerpha.refund.repository.RefundRepository;
import com.cerpha.orderservice.cerpha.refund.request.RefundRequest;
import com.cerpha.orderservice.common.client.product.ProductClient;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.cerpha.orderservice.cerpha.order.domain.OrderStatus.REFUNDING;
import static com.cerpha.orderservice.common.exception.ExceptionCode.NOT_AVAILABLE_REFUND;
import static com.cerpha.orderservice.common.exception.ExceptionCode.NOT_FOUND_ORDER;

@Slf4j
@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductClient productClient;

    public RefundService(RefundRepository refundRepository, OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductClient productClient) {
        this.refundRepository = refundRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productClient = productClient;
    }

    @Transactional
    public void refundOrder(RefundRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_ORDER));

        if (!order.isRefundable(LocalDate.now())) {
            throw new BusinessException(NOT_AVAILABLE_REFUND);
        }

        order.requestRefund();

        Refund refund = Refund.builder()
                .order(order)
                .reason(request.getRefundReason())
                .build();

        refundRepository.save(refund);
    }

    @Scheduled(cron = "${env.order.changeStatusCycle}")
    @Transactional
    public void finishRefunding() {
        log.info("Finish RefundingOrder");
        LocalDate yesterday = LocalDate.now().minusDays(1);

        LocalDateTime start = yesterday.atStartOfDay();
        LocalDateTime end = yesterday.atTime(LocalTime.MAX);

        List<Order> refundingOrders = orderRepository.findOrdersByStatusAndUpdatedAtBetween(REFUNDING, start, end);
        log.info("refundingOrders={}", refundingOrders.size());

        List<ProductUnitCountRequest> productUnitCountRequests = new ArrayList<>();
        refundingOrders
                .forEach(o -> {
                    List<OrderProduct> orderProducts = orderProductRepository.findOrderProductsByOrderId(o.getId());
                    orderProducts.forEach(orderProduct ->
                            productUnitCountRequests.add(new ProductUnitCountRequest(orderProduct.getProductId(), orderProduct.getUnitCount())));
                });

        RestoreStockRequest restoreStockRequest = new RestoreStockRequest(productUnitCountRequests);
        productClient.restoreStock(restoreStockRequest);

        refundingOrders.forEach(Order::finishRefund);
    }
}
