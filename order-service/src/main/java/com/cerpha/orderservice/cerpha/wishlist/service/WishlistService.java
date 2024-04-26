package com.cerpha.orderservice.cerpha.wishlist.service;

import com.cerpha.orderservice.cerpha.wishlist.domain.Wishlist;
import com.cerpha.orderservice.cerpha.wishlist.repository.WishlistRepository;
import com.cerpha.orderservice.cerpha.wishlist.request.AddWishlistRequest;
import com.cerpha.orderservice.cerpha.wishlist.request.DeleteWishlistRequest;
import com.cerpha.orderservice.cerpha.wishlist.request.UpdateWishlistRequest;
import com.cerpha.orderservice.cerpha.wishlist.response.AllWishlistResponse;
import com.cerpha.orderservice.cerpha.wishlist.response.WishlistResponse;
import com.cerpha.orderservice.common.client.product.ProductClient;
import com.cerpha.orderservice.common.client.product.response.ProductDetailResponse;
import com.cerpha.orderservice.common.client.user.UserClient;
import com.cerpha.orderservice.common.exception.BusinessException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.cerpha.orderservice.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    public WishlistService(WishlistRepository wishlistRepository,
                           UserClient userClient, ProductClient productClient) {
        this.wishlistRepository = wishlistRepository;
        this.userClient = userClient;
        this.productClient = productClient;
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "addWishlistFallback")
    @Retry(name = "product-service")
    @Transactional
    public void addWishlist(AddWishlistRequest request) {
        if (wishlistRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId()).isPresent()) {
            throw new BusinessException(DUPLICATED_WISHLIST_PRODUCT);
        }

        Long userId = userClient.getUserId(request.getUserId()).getResultData();
        ProductDetailResponse productDetail = productClient.getProductForWishlist(request.getProductId()).getResultData();

        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .productId(productDetail.getProductId())
                .unitCount(request.getUnitCount())
                .productName(productDetail.getProductName())
                .price(productDetail.getProductPrice() * request.getUnitCount())
                .build();

        wishlistRepository.save(wishlist);
    }

    public void addWishlistFallback(AddWishlistRequest request, Throwable e) {
        log.error(e.getMessage());
        throw new BusinessException(NOT_AVAILABLE_ADD_WISHLIST);
    }

    @Transactional(readOnly = true)
    public AllWishlistResponse getWishlists(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserIdOrderByUpdatedAtDesc(userId);

        List<WishlistResponse> wishlistResponses = wishlists.stream()
                .map(wishlist -> WishlistResponse.builder()
                        .wishlistId(wishlist.getId())
                        .productId(wishlist.getProductId())
                        .unitCount(wishlist.getUnitCount())
                        .productName(wishlist.getProductName())
                        .productPrice(wishlist.getPrice())
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
        Wishlist wishlist = wishlistRepository.findById(request.getWishlistId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_WISHLIST));

        if (!Objects.equals(request.getUserId(), wishlist.getUserId())) {
            throw new BusinessException(NOT_OWN_WISHLIST);
        }

        wishlist.changeProductUnitCount(request.getUnitCount());
    }

    @Transactional
    public void deleteWishlist(DeleteWishlistRequest request) {
        Wishlist wishlist = wishlistRepository.findById(request.getWishlistId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_WISHLIST));

        if (!Objects.equals(request.getUserId(), wishlist.getUserId())) {
            throw new BusinessException(NOT_OWN_WISHLIST);
        }

        wishlistRepository.deleteById(request.getWishlistId());
    }

    @Transactional
    public void deleteAllWishList(Long userId) {
        wishlistRepository.deleteByUserId(userId);
    }

    private long getTotalPrice(List<Wishlist> wishlists) {
        return wishlists.stream()
                .mapToLong(Wishlist::getPrice)
                .sum();
    }

}
