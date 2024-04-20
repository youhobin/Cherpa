package com.cerpha.cerphaproject.cerpha.order.repository;

import com.cerpha.cerphaproject.cerpha.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
