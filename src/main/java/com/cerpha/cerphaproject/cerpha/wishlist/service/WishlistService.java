package com.cerpha.cerphaproject.cerpha.wishlist.service;

import com.cerpha.cerphaproject.cerpha.product.domain.Product;
import com.cerpha.cerphaproject.cerpha.product.repository.ProductRepository;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.user.repository.UserRepository;
import com.cerpha.cerphaproject.cerpha.wishlist.domain.Wishlist;
import com.cerpha.cerphaproject.cerpha.wishlist.repository.WishlistRepository;
import com.cerpha.cerphaproject.cerpha.wishlist.request.AddWishlistRequest;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cerpha.cerphaproject.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void addWishlist(Long userId, AddWishlistRequest request) {
        if (wishlistRepository.findByUserIdAndProductId(userId, request.getProductId()).isPresent()) {
            throw new BusinessException(DUPLICATED_WISHLIST_PRODUCT);
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .unitCount(request.getUnitCount())
                .build();

        wishlistRepository.save(wishlist);
    }
}
