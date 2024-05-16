package com.cerpha.paymentservice.cerpha.payment.repository;

import com.cerpha.paymentservice.cerpha.payment.domain.Payment;
import com.cerpha.paymentservice.cerpha.payment.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderIdAndPaymentStatus(Long orderId, PaymentStatus paymentStatus);

    void deleteByOrderId(Long orderId);
}
