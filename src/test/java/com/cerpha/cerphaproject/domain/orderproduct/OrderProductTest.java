package com.cerpha.cerphaproject.domain.orderproduct;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import com.cerpha.cerphaproject.cerpha.order.domain.OrderProduct;
import com.cerpha.cerphaproject.cerpha.order.domain.OrderStatus;
import com.cerpha.cerphaproject.cerpha.product.domain.Product;
import com.cerpha.cerphaproject.cerpha.user.domain.UserRole;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderProductTest {

    @DisplayName("주문 상품을 등록한다.")
    @Test
    public void addOrderProduct() {
        // given
        String productName = "신발";
        long productPrice = 10000;
        long productStock = 10;
        Product product = createProduct(productName, productPrice, productStock);

        // when
        long unitCount = 5L;
        OrderProduct orderProduct = OrderProduct.addOrderProduct(product, unitCount);

        // then
        Assertions.assertThat(orderProduct.getUnitCount()).isEqualTo(unitCount);
        Assertions.assertThat(orderProduct.getProduct().getName()).isEqualTo("신발");
        Assertions.assertThat(orderProduct.getProduct().getPrice()).isEqualTo(10000L);
    }

    @DisplayName("주문 상품을 등록 시 상품의 재고가 감소한다.")
    @Test
    public void addOrderProductWithDecreaseProduct() {
        // given
        String productName = "신발";
        long productPrice = 10000;
        long productStock = 10;
        Product product = createProduct(productName, productPrice, productStock);

        // when
        long unitCount = 4;
        OrderProduct orderProduct = OrderProduct.addOrderProduct(product, unitCount);

        // then
        Assertions.assertThat(orderProduct.getProduct().getStock()).isEqualTo(productStock - unitCount);
    }

    @DisplayName("주문 상품을 등록 시 상품의 재고가 더 적으면 예외가 발생한다.")
    @Test
    public void addOrderProductWithDecreaseProduct_Exception() {
        // given
        String productName = "신발";
        long productPrice = 10000;
        long productStock = 10;
        Product product = createProduct(productName, productPrice, productStock);

        // when // then
        long unitCount = 11;
        Assertions.assertThatThrownBy(() -> OrderProduct.addOrderProduct(product, unitCount))
                .isInstanceOf(BusinessException.class)
                .hasMessage("상품의 재고가 부족합니다.");
    }

    @DisplayName("주문 상품을 등록 시 주문을 등록한다.")
    @Test
    public void addOrderProductWithOrder() {
        // given
        String productName = "신발";
        long productPrice = 10000;
        long productStock = 10;
        long unitCount = 3;
        Product product = createProduct(productName, productPrice, productStock);
        OrderProduct orderProduct = OrderProduct.addOrderProduct(product, unitCount);
        Order paymentOrder = createPaymentOrder();

        // when
        orderProduct.addOrder(paymentOrder);

        // then
        Assertions.assertThat(orderProduct.getOrder().getStatus()).isEqualTo(OrderStatus.PAYMENT);
        Assertions.assertThat(orderProduct.getOrder().getTotalPrice()).isEqualTo(10000);
    }

    private static Product createProduct(String productName, Long productPrice, Long productStock) {
        return new Product(1L, productName, "신발입니다.", productPrice, productStock, "hobin");
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
