package com.cerpha.cerphaproject.cerpha.order.repository;

import com.cerpha.cerphaproject.cerpha.order.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
