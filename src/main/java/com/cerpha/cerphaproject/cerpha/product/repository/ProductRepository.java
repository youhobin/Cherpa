package com.cerpha.cerphaproject.cerpha.product.repository;

import com.cerpha.cerphaproject.cerpha.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
