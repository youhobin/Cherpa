package com.cerpha.cerphaproject.cerpha.wishlist.service;

import com.cerpha.cerphaproject.cerpha.product.domain.Product;
import com.cerpha.cerphaproject.cerpha.product.repository.ProductRepository;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.user.repository.UserRepository;
import com.cerpha.cerphaproject.cerpha.wishlist.domain.Wishlist;
import com.cerpha.cerphaproject.cerpha.wishlist.repository.WishlistRepository;
import com.cerpha.cerphaproject.cerpha.wishlist.request.AddWishlistRequest;
import com.cerpha.cerphaproject.cerpha.wishlist.request.DeleteWishlistRequest;
import com.cerpha.cerphaproject.cerpha.wishlist.request.UpdateWishlistRequest;
import com.cerpha.cerphaproject.cerpha.wishlist.response.AllWishlistResponse;
import com.cerpha.cerphaproject.cerpha.wishlist.response.WishlistResponse;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void addWishlist(AddWishlistRequest request) {
        if (wishlistRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId()).isPresent()) {
            throw new BusinessException(DUPLICATED_WISHLIST_PRODUCT);
        }

        Users user = userRepository.findById(request.getUserId())
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

    @Transactional(readOnly = true)
    public AllWishlistResponse getWishlists(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserIdWithProduct(userId);

        List<WishlistResponse> wishlistResponses = wishlists.stream()
                .map(w -> WishlistResponse.builder()
                        .productId(w.getProduct().getId())
                        .unitCount(w.getUnitCount())
                        .productName(w.getProduct().getName())
                        .productPrice(w.getProduct().getPrice())
                        .build())
                .toList();

        long totalPrice = getTotalPrice(wishlists);

        return AllWishlistResponse.builder()
                .userId(userId)
                .totalPrice(totalPrice)
                .wishlist(wishlistResponses)
                .build();
    }

    @Transactional
    public void updateWishlist(UpdateWishlistRequest request) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_WISHLIST));

        wishlist.changeProductUnitCount(request.getUnitCount());
    }

    @Transactional
    public void deleteWishlist(DeleteWishlistRequest request) {
        if (wishlistRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId()).isEmpty()) {
            throw new BusinessException(NOT_FOUND_WISHLIST);
        }

        wishlistRepository.deleteByUserIdAndProductId(request.getUserId(), request.getProductId());
    }

    @Transactional
    public void deleteAllWishList(Long userId) {
        wishlistRepository.deleteByUserId(userId);
    }

    private long getTotalPrice(List<Wishlist> wishlists) {
        return wishlists.stream()
                .mapToLong(w -> w.getUnitCount() * w.getProduct().getPrice())
                .sum();
    }
}
