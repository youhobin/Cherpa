package com.cerpha.cerphaproject.cerpha.product.repository;

import com.cerpha.cerphaproject.cerpha.product.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Products, Long> {
}
