package com.cerpha.cerphaproject.cerpha.order.repository;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import com.cerpha.cerphaproject.cerpha.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.status = :orderStatus AND o.updatedAt BETWEEN :start AND :end")
    List<Order> findOrdersByStatusAndUpdatedAtBetween(OrderStatus orderStatus, LocalDateTime start, LocalDateTime end);
}
