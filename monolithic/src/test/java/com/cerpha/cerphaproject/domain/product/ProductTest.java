package com.cerpha.cerphaproject.domain.product;

import com.cerpha.cerphaproject.cerpha.product.domain.Product;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @DisplayName("상품의 재고를 다시 채운다.")
    @Test
    public void restore() {
        // given
        Product product = new Product(1L, "신발", "신발입니다.", 10000L, 10L, "hobin");

        // when
        product.restoreStock(10L);

        // then
        Assertions.assertThat(product.getStock()).isEqualTo(20L);
    }

    @DisplayName("상품 주문 시 재고를 감소시킨다.")
    @Test
    public void decreaseStock() {
        // given
        Product product = new Product(1L, "신발", "신발입니다.", 10000L, 10L, "hobin");

        //when
        product.decreaseStock(5L);

        //given
        Assertions.assertThat(product.getStock()).isEqualTo(5L);
    }

    @DisplayName("재고보다 많은 수량으로 차감을 시도하면 예외가 발생한다.")
    @Test
    public void decreaseStockWithException() {
        // given
        Product product = new Product(1L, "신발", "신발입니다.", 10000L, 10L, "hobin");

        // when // then
        Assertions.assertThatThrownBy(() -> product.decreaseStock(11L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("상품의 재고가 부족합니다.");
    }
}
