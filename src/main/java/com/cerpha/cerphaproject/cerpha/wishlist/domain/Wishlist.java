package com.cerpha.cerphaproject.cerpha.wishlist.domain;

import com.cerpha.cerphaproject.cerpha.BaseTimeEntity;
import com.cerpha.cerphaproject.cerpha.product.domain.Product;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Long unitCount;

    @Builder
    public Wishlist(Users user, Product product, Long unitCount) {
        this.user = user;
        this.product = product;
        this.unitCount = unitCount;
    }

    public void changeProductUnitCount(Long changedUnitCount) {
        this.unitCount = changedUnitCount;
    }
}
