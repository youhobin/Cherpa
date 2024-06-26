package com.cerpha.orderservice.cerpha.wishlist.domain;

import com.cerpha.orderservice.cerpha.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    private Long unitCount;
    private String productName;
    private Long price;

    @Builder
    public Wishlist(Long userId, Long productId, Long unitCount, String productName, Long price) {
        this.userId = userId;
        this.productId = productId;
        this.unitCount = unitCount;
        this.productName = productName;
        this.price = price;
    }

    public void changeProductUnitCount(Long changedUnitCount) {
        this.unitCount = changedUnitCount;
    }
}
