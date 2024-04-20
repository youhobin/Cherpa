package com.cerpha.cerphaproject.cerpha.refund.service;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import com.cerpha.cerphaproject.cerpha.order.repository.OrderRepository;
import com.cerpha.cerphaproject.cerpha.refund.domain.Refund;
import com.cerpha.cerphaproject.cerpha.refund.repository.RefundRepository;
import com.cerpha.cerphaproject.cerpha.refund.request.RefundRequest;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.cerpha.cerphaproject.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;

    public RefundService(RefundRepository refundRepository, OrderRepository orderRepository) {
        this.refundRepository = refundRepository;
        this.orderRepository = orderRepository;
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
}
