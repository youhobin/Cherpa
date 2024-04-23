package com.cerpha.cerphaproject.cerpha.product.domain;

import com.cerpha.cerphaproject.cerpha.BaseTimeEntity;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.cerpha.cerphaproject.common.exception.ExceptionCode.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

        if (remainingStock < 0) {
            throw new BusinessException(OUT_OF_STOCK);
        }
        this.stock = remainingStock;
    }

    public void restoreStock(Long unitCount) {
        this.stock += unitCount;
    }
}
