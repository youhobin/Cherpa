package com.cerpha.cerphaproject.cerpha.refund.service;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import com.cerpha.cerphaproject.cerpha.order.domain.OrderProduct;
import com.cerpha.cerphaproject.cerpha.order.domain.OrderStatus;
import com.cerpha.cerphaproject.cerpha.order.repository.OrderProductRepository;
import com.cerpha.cerphaproject.cerpha.order.repository.OrderRepository;
import com.cerpha.cerphaproject.cerpha.refund.domain.Refund;
import com.cerpha.cerphaproject.cerpha.refund.repository.RefundRepository;
import com.cerpha.cerphaproject.cerpha.refund.request.RefundRequest;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.cerpha.cerphaproject.cerpha.order.domain.OrderStatus.*;
import static com.cerpha.cerphaproject.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public RefundService(RefundRepository refundRepository, OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.refundRepository = refundRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
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

        refundingOrders
                .forEach(o -> {
                    List<OrderProduct> orderProducts = orderProductRepository.findOrderProductsByOrderId(o.getId());
                    orderProducts.forEach(orderProduct -> orderProduct.getProduct().restoreStock(orderProduct.getUnitCount()));
                });

        refundingOrders.forEach(Order::finishRefund);
    }
}
