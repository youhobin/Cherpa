package com.cerpha.orderservice.cerpha.refund.repository;

import com.cerpha.orderservice.cerpha.refund.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
