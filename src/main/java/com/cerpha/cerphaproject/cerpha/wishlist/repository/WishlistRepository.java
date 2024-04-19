package com.cerpha.cerphaproject.cerpha.wishlist.repository;

import com.cerpha.cerphaproject.cerpha.wishlist.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);

    @Query("select w from Wishlist w join fetch w.product where w.user.id = :userId")
    List<Wishlist> findByUserIdWithProduct(Long userId);

    void deleteByUserIdAndProductId(Long userId, Long productId);
}
