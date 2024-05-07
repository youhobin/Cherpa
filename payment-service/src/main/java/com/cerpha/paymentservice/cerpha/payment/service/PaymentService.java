package com.cerpha.paymentservice.cerpha.payment.service;

import com.cerpha.paymentservice.cerpha.payment.domain.Payment;
import com.cerpha.paymentservice.cerpha.payment.domain.PaymentStatus;
import com.cerpha.paymentservice.cerpha.payment.repository.PaymentRepository;
import com.cerpha.paymentservice.cerpha.payment.request.CompletePaymentRequest;
import com.cerpha.paymentservice.cerpha.payment.request.ProcessPaymentRequest;
import com.cerpha.paymentservice.common.client.OrderClient;
import com.cerpha.paymentservice.common.client.product.ProductClient;
import com.cerpha.paymentservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.paymentservice.common.exception.BusinessException;
import com.cerpha.paymentservice.common.exception.ExceptionCode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cerpha.paymentservice.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final ProductClient productClient;
    private final PaymentProducer paymentProducer;

    public PaymentService(PaymentRepository paymentRepository, OrderClient orderClient, ProductClient productClient, PaymentProducer paymentProducer) {
        this.paymentRepository = paymentRepository;
        this.orderClient = orderClient;
        this.productClient = productClient;
        this.paymentProducer = paymentProducer;
    }


    @Transactional
    public void processPayment(ProcessPaymentRequest request) {
        makePaymentException(request);

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .paymentStatus(PaymentStatus.PAYMENT_WAIT)
                .build();

        paymentRepository.save(payment);
    }

//    @CircuitBreaker(name = "order-service", fallbackMethod = "completePaymentFallback")
//    @Retry(name = "order-service")
    @Transactional
    public void completePayment(CompletePaymentRequest request) {
        Payment payment = paymentRepository.findByOrderIdAndPaymentStatus(request.getOrderId(), PaymentStatus.PAYMENT_WAIT)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_PAYMENT));

        // 결제 실패 상황 발생
        if ("FAIL".equals(request.getPaymentMethod())) {
            paymentProducer.rollbackCreatedOrder(request.getOrderId());
            throw new BusinessException(ExceptionCode.PAYMENT_FAIl);
        }

        payment.addPaymentMethod(request.getPaymentMethod());

        // 결제 완료 시 주문 완료
        paymentProducer.completePayment(request.getOrderId());
    }

//    @CircuitBreaker(name = "order-service", fallbackMethod = "completePaymentFallback")
//    @Retry(name = "order-service")
//    @Transactional
//    public void completePayment(CompletePaymentRequest request) {
//        Payment payment = paymentRepository.findByOrderIdAndPaymentStatus(request.getOrderId(), PaymentStatus.PAYMENT_WAIT)
//                .orElseThrow(() -> new BusinessException(NOT_FOUND_PAYMENT));
//
//        // 결제 실패 상황 발생
//        if ("FAIL".equals(request.getPaymentMethod())) {
//            orderClient.cancelOrder(request.getOrderId());
//            throw new BusinessException(ExceptionCode.PAYMENT_FAIl);
//        }
//
//        payment.addPaymentMethod(request.getPaymentMethod());
//
//        // 결제 완료 시 주문 완료
//        orderClient.completeOrderPayment(request.getOrderId());
//    }

    @Transactional
    public void deletePaymentByOrderId(Long orderId) {
        paymentRepository.deleteByOrderId(orderId);
    }

    public void completePaymentFallback(CompletePaymentRequest request, BusinessException e) {
        throw new BusinessException(e.getExceptionCode());
    }

    private void makePaymentException(ProcessPaymentRequest request) {
        // 20 퍼센트는 결제 취소
        if (request.getUserId() % 5 == 0) {
            throw new BusinessException(CHANGE_MIND);
        }
    }
}
