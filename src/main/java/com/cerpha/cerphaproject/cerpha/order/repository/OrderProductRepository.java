package com.cerpha.cerphaproject.cerpha.order.repository;

import com.cerpha.cerphaproject.cerpha.order.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op FROM OrderProduct op WHERE op.order.user.id = :userId")
    List<OrderProduct> findOrderProductsByUserId(Long userId);

    List<OrderProduct> findOrderProductsByOrderId(Long orderId);
}
