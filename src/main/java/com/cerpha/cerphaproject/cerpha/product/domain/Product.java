package com.cerpha.cerphaproject.cerpha.product.domain;

import com.cerpha.cerphaproject.cerpha.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String name;

    private String description;
    private Long price;
    private Long stock;
    private String producer;

    public void decreaseStock(Long unitCount) {
        long remainingStock = this.stock - unitCount;
        // todo 재고가 0보다 작게되면 exception 발생
        this.stock = remainingStock;
    }

    public void restoreStock(Long unitCount) {
        this.stock += unitCount;
    }
}
