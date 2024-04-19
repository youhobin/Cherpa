package com.cerpha.cerphaproject.cerpha.wishlist.repository;

import com.cerpha.cerphaproject.cerpha.wishlist.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);

}
