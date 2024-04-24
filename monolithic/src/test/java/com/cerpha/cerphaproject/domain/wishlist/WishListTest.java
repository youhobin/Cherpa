package com.cerpha.cerphaproject.domain.wishlist;

import com.cerpha.cerphaproject.cerpha.product.domain.Product;
import com.cerpha.cerphaproject.cerpha.user.domain.UserRole;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.wishlist.domain.Wishlist;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WishListTest {

    @DisplayName("wishlist의 수량을 변경한다.")
    @Test
    public void changeProductUnitCount() {
        // given
        Product product = new Product(1L, "신발", "신발입니다.", 10000L, 10L, "hobin");
        Users user = Users.builder()
                .email("ghdb132@naver.com")
                .password("132465aa")
                .name("hobin")
                .nickname("ghgh")
                .phone("01012345678")
                .address("인천시")
                .role(UserRole.USER)
                .build();

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .unitCount(5L)
                .build();

        // when
        wishlist.changeProductUnitCount(7L);

        // then
        Assertions.assertThat(wishlist.getUnitCount()).isEqualTo(7L);

    }
}
