package com.cerpha.orderservice.cerpha.order.domain;

import com.cerpha.orderservice.cerpha.BaseTimeEntity;
import com.cerpha.orderservice.common.converter.EncryptionConverter;
import com.cerpha.orderservice.common.exception.BusinessException;
import com.cerpha.orderservice.common.exception.ExceptionCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

import static com.cerpha.orderservice.cerpha.order.domain.OrderStatus.*;
import static com.cerpha.orderservice.common.exception.ExceptionCode.NOT_AVAILABLE_PAYMENT;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Convert(converter = EncryptionConverter.class)
    private String deliveryAddress;

    @Convert(converter = EncryptionConverter.class)
    private String deliveryPhone;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "user_id")
    private Long userId;

    @Builder
    public Order(String deliveryAddress, String deliveryPhone, OrderStatus status, Long userId) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryPhone = deliveryPhone;
        this.status = status;
        this.userId = userId;
    }

    public void cancel() {
        if (this.status.equals(PAYMENT) || this.status.equals(PAYMENT_WAITING)) {
            this.status = CANCEL;
            return;
        }

        throw new BusinessException(ExceptionCode.NOT_AVAILABLE_CANCEL);
    }

    public boolean isRefundable(LocalDate now) {
        LocalDate updatedAt = this.getUpdatedAt().toLocalDate();

        int daysDiff = Period.between(updatedAt, now).getDays();

        if (this.status.equals(DONE) && daysDiff <= 1) {
            return true;
        }

        return false;
    }

    public void requestRefund() {
        this.status = REFUNDING;
    }

    public void finishDelivery() {
        this.status = DONE;
    }

    public void startDelivery() {
        this.status = SHIPPING;
    }

    public void finishRefund() {
        this.status = REFUNDED;
    }

    public void completeOrderPayment() {
        if (!this.status.equals(PAYMENT_WAITING)) {
            throw new BusinessException(NOT_AVAILABLE_PAYMENT);
        }
        this.status = PAYMENT;
    }
}
