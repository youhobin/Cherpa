package com.cerpha.paymentservice.cerpha.payment.domain;

import com.cerpha.paymentservice.cerpha.BaseTimeEntity;
import com.cerpha.paymentservice.common.exception.BusinessException;
import com.cerpha.paymentservice.common.exception.ExceptionCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payments")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private Long orderId;

    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Builder
    public Payment(Long orderId, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
    }

    public void addPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PAYMENT_COMPLETED;
    }
}
