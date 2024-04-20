package com.cerpha.cerphaproject.cerpha.order.repository;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
