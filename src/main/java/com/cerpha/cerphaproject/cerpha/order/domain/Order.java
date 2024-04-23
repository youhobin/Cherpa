package com.cerpha.cerphaproject.cerpha.order.domain;

import com.cerpha.cerphaproject.cerpha.BaseTimeEntity;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.common.converter.EncryptionConverter;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static com.cerpha.cerphaproject.cerpha.order.domain.OrderStatus.*;
import static jakarta.persistence.FetchType.LAZY;

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

    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Builder
    public Order(String deliveryAddress, String deliveryPhone, Long totalPrice, OrderStatus status, Users user) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryPhone = deliveryPhone;
        this.totalPrice = totalPrice;
        this.status = status;
        this.user = user;
    }

    public void cancel() {
        if (!this.status.equals(PAYMENT)) {
            throw new BusinessException(ExceptionCode.NOT_AVAILABLE_CANCEL);
        }

        this.status = CANCEL;
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
}
