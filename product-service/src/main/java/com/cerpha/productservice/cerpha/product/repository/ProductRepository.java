package com.cerpha.productservice.cerpha.product.repository;

import com.cerpha.productservice.cerpha.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findById(Long productId);


    List<Product> findByIdIn(List<Long> productIds);
}
