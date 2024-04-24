package com.cerpha.cerphaproject.cerpha.order.domain;

import com.cerpha.cerphaproject.cerpha.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Long unitCount;
    private Long unitPrice;

    public static OrderProduct addOrderProduct(Product product, Long unitCount) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.addProduct(product, unitCount);
        product.decreaseStock(unitCount);
        return orderProduct;
    }

    private void addProduct(Product product, Long unitCount) {
        this.product = product;
        this.unitCount = unitCount;
        this.unitPrice = product.getPrice();
    }

    public void addOrder(Order order) {
        this.order = order;
    }

}
