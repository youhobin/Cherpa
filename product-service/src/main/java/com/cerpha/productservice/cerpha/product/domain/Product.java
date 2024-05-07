package com.cerpha.productservice.cerpha.product.domain;

import com.cerpha.productservice.cerpha.BaseTimeEntity;
import com.cerpha.productservice.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import static com.cerpha.productservice.common.exception.ExceptionCode.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "product_type")
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

    @Builder
    public Product(String name, String description, Long price, Long stock, String producer) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.producer = producer;
    }

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
