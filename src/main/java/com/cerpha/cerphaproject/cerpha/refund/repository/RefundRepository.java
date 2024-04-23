package com.cerpha.cerphaproject.cerpha.refund.repository;

import com.cerpha.cerphaproject.cerpha.refund.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
