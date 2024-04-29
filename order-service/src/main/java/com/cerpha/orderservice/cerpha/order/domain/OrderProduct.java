package com.cerpha.orderservice.cerpha.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(name = "product_id")
    private Long productId;

    private Long unitCount;
    private Long orderProductPrice;
    private String productName;

    @Builder
    public OrderProduct(Order order, Long productId, Long unitCount, Long orderProductPrice, String productName) {
        this.order = order;
        this.productId = productId;
        this.unitCount = unitCount;
        this.orderProductPrice = orderProductPrice;
        this.productName = productName;
    }

//    public static OrderProduct addOrderProduct(Long productId, Long unitCount) {
//        OrderProduct orderProduct = new OrderProduct();
//        orderProduct.addProduct(productId, unitCount);
//        product.decreaseStock(unitCount);
//        return orderProduct;
//    }
//
//    private void addProduct(Product product, Long unitCount) {
//        this.product = product;
//        this.unitCount = unitCount;
//        this.unitPrice = product.getPrice();
//    }
//
//    public void addOrder(Order order) {
//        this.order = order;
//    }

}
