package com.cerpha.cerphaproject.domain.order;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import com.cerpha.cerphaproject.cerpha.order.domain.OrderStatus;
import com.cerpha.cerphaproject.cerpha.user.domain.UserRole;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderTest {

    @DisplayName("주문을 취소하면 상태를 CANCEL로 변경한다.")
    @Test
    public void cancelOrder() {
        // given
        Order order = createPaymentOrder();

        // when
        order.cancel();

        // then
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @DisplayName("PAYMENT 상태가 아닌 주문 취소 시 예외가 발생한다.")
    @Test
    public void cancelOrderWithException() {
        // given
        Order order = createDoneOrder();

        // when // then
        Assertions.assertThatThrownBy(order::cancel)
                .isInstanceOf(BusinessException.class)
                .hasMessage("주문을 취소할 수 없는 상태입니다.");

    }

    @DisplayName("반품 가능한지 확인한다.")
    @Test
    public void isRefundTrue() {
        // given
        Order order = createDoneOrder();
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0);
        order.setUpdatedAt(updatedAt);

        // when
        LocalDate updatedAtPlusDay = LocalDate.of(2024, 1, 2);
        boolean isRefundable = order.isRefundable(updatedAtPlusDay);

        // then
        Assertions.assertThat(isRefundable).isTrue();
    }

    @DisplayName("주문 status가 DONE이 아니면 반품 불가한 상태이다.")
    @Test
    public void isRefundFalse_Status() {
        // given
        Order order = createPaymentOrder();
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0);
        order.setUpdatedAt(updatedAt);

        // when
        LocalDate updatedAtPlusDay = LocalDate.of(2024, 1, 2);
        boolean isRefundable = order.isRefundable(updatedAtPlusDay);

        // then
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT);
        Assertions.assertThat(isRefundable).isFalse();
    }

    @DisplayName("주문 status가 DONE이어도 수정 일시가 2일 이상이면 넘었으면 반품 불가능한 상태이다.")
    @Test
    public void isRefundFalse_afterTwoDay() {
        // given
        Order order = createDoneOrder();
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0);
        order.setUpdatedAt(updatedAt);

        // when
        LocalDate updatedAtPlusDay = LocalDate.of(2024, 1, 3);
        boolean isRefundable = order.isRefundable(updatedAtPlusDay);

        // then
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.DONE);
        Assertions.assertThat(isRefundable).isFalse();
    }

    @DisplayName("주문 반품을 요청하면 status는 REFUNDING이다.")
    @Test
    public void requestRefund() {
        // given
        Order order = createPaymentOrder();

        // when
        order.requestRefund();

        // then
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.REFUNDING);
    }

    @DisplayName("주문 반품이 끝나면 status는 REFUNDED이다.")
    @Test
    public void finishRefund() {
        // given
        Order order = createPaymentOrder();

        // when
        order.finishRefund();

        // then
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.REFUNDED);
    }

    @DisplayName("주문 배송이 시작하면 status는 SHIPPING이다.")
    @Test
    public void startDelivery() {
        // given
        Order order = createPaymentOrder();

        // when
        order.startDelivery();

        // then
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPING);
    }

    @DisplayName("주문 배송이 끝나면 status는 DONE이다.")
    @Test
    public void finishDelivery() {
        // given
        Order order = createPaymentOrder();

        // when
        order.finishDelivery();

        // then
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.DONE);
    }

    private Order createDoneOrder() {
        Users user = Users.builder()
                .email("ghdb132@naver.com")
                .password("132465aa")
                .name("hobin")
                .nickname("ghgh")
                .phone("01012345678")
                .address("인천시")
                .role(UserRole.USER)
                .build();

        return Order.builder()
                .deliveryPhone(user.getPhone())
                .deliveryAddress(user.getAddress())
                .totalPrice(10000L)
                .status(OrderStatus.DONE)
                .user(user)
                .build();
    }

    private Order createPaymentOrder() {
        Users user = Users.builder()
                .email("ghdb132@naver.com")
                .password("132465aa")
                .name("hobin")
                .nickname("ghgh")
                .phone("01012345678")
                .address("인천시")
                .role(UserRole.USER)
                .build();

        return Order.builder()
                .deliveryPhone(user.getPhone())
                .deliveryAddress(user.getAddress())
                .totalPrice(10000L)
                .status(OrderStatus.PAYMENT)
                .user(user)
                .build();
    }
}
