package com.cerpha.cerphaproject.cerpha.order.domain;

import com.cerpha.cerphaproject.cerpha.BaseTimeEntity;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.common.converter.EncryptionConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.cerpha.cerphaproject.cerpha.order.domain.OrderStatus.*;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Convert(converter = EncryptionConverter.class)
    private String deliveryAddress;

    @Convert(converter = EncryptionConverter.class)
    private String deliveryPhone;

    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;

    @Builder
    public Order(String deliveryAddress, String deliveryPhone, Long totalPrice, OrderStatus status, Users user, List<OrderProduct> orderProducts) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryPhone = deliveryPhone;
        this.totalPrice = totalPrice;
        this.status = status;
        this.user = user;
        this.orderProducts = orderProducts;
    }

    public static Order addOrder(String deliveryAddress, String deliveryPhone, Users user, List<OrderProduct> orderProducts) {
        Order order = new Order();
        order.addInfo(deliveryAddress, deliveryPhone);
        order.addUser(user);
        order.addOrderProducts(orderProducts);
        order.addTotalPrice(orderProducts);
        return order;
    }

    private void addTotalPrice(List<OrderProduct> orderProducts) {
        this.totalPrice = orderProducts.stream()
                .mapToLong(p -> p.getUnitPrice() * p.getUnitCount())
                .sum();
    }

    private void addOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
        orderProducts.forEach(p -> p.addOrder(this));
    }

    private void addUser(Users user) {
        this.user = user;
    }

    private void addInfo(String deliveryAddress, String deliveryPhone) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryPhone = deliveryPhone;
        this.status = PAYMENT;
    }


}
