package com.cerpha.cerphaproject.cerpha.product;

import com.cerpha.cerphaproject.cerpha.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Products extends BaseTimeEntity {

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

}
