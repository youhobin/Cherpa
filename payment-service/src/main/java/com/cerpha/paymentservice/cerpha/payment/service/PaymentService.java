package com.cerpha.paymentservice.cerpha.payment.service;

import com.cerpha.paymentservice.cerpha.payment.domain.Payment;
import com.cerpha.paymentservice.cerpha.payment.domain.PaymentStatus;
import com.cerpha.paymentservice.cerpha.payment.repository.PaymentRepository;
import com.cerpha.paymentservice.cerpha.payment.request.CompletePaymentRequest;
import com.cerpha.paymentservice.cerpha.payment.request.ProcessPaymentRequest;
import com.cerpha.paymentservice.common.exception.BusinessException;
import com.cerpha.paymentservice.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cerpha.paymentservice.common.exception.ExceptionCode.CHANGE_MIND;
import static com.cerpha.paymentservice.common.exception.ExceptionCode.NOT_FOUND_PAYMENT;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    public PaymentService(PaymentRepository paymentRepository, PaymentProducer paymentProducer) {
        this.paymentRepository = paymentRepository;
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

    @Transactional
    public void completePayment(CompletePaymentRequest request) {
        Payment payment = paymentRepository.findByOrderIdAndPaymentStatus(request.getOrderId(), PaymentStatus.PAYMENT_WAIT)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_PAYMENT));

        // 결제 실패 상황 발생
        if ("FAIL".equals(request.getPaymentMethod())) {
            paymentProducer.cancelCreatedOrder(request.getOrderId());
            throw new BusinessException(ExceptionCode.PAYMENT_FAIl);
        }

        payment.addPaymentMethod(request.getPaymentMethod());

        // 결제 완료 시 주문 완료
        paymentProducer.completePayment(request.getOrderId());
    }

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
