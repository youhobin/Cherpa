package com.cerpha.productservice.cerpha.product.service;

import com.cerpha.productservice.cerpha.product.domain.Product;
import com.cerpha.productservice.cerpha.product.repository.ProductRepository;
import com.cerpha.productservice.cerpha.product.request.*;
import com.cerpha.productservice.cerpha.product.response.*;
import com.cerpha.productservice.common.client.exception.FeignClientException;
import com.cerpha.productservice.common.client.payment.PaymentClient;
import com.cerpha.productservice.common.client.payment.request.ProcessPaymentRequest;
import com.cerpha.productservice.common.dto.PageResponseDto;
import com.cerpha.productservice.common.exception.BusinessException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.cerpha.productservice.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockService productStockService;
    private final PaymentClient paymentClient;
    private final ProductProducer productProducer;

    public ProductService(ProductRepository productRepository, ProductStockService productStockService, PaymentClient paymentClient, ProductProducer productProducer) {
        this.productRepository = productRepository;
        this.productStockService = productStockService;
        this.paymentClient = paymentClient;
        this.productProducer = productProducer;
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ProductListResponse> getAllProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Product> productsPage = productRepository.findAll(pageRequest);
        List<ProductListResponse> products = productsPage.stream()
                .map(p -> ProductListResponse.builder()
                        .id(p.getId())
                        .producer(p.getProducer())
                        .price(p.getPrice())
                        .name(p.getName())
                        .build())
                .toList();

        return new PageResponseDto<ProductListResponse>(
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast(),
                products);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .producer(product.getProducer())
                .build();
    }

    @Transactional
    public OrderProductListResponse getOrderProductsDetail(OrderProductListRequest request) {
        List<AddOrderProductResponse> orderProductResponses = request.getOrderProducts().stream()
                .map(op -> {
                    Product product = productRepository.findById(op.getProductId())
                            .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

                    return AddOrderProductResponse.builder()
                            .productId(product.getId())
                            .unitCount(op.getUnitCount())
                            .price(product.getPrice())
                            .productName(product.getName())
                            .build();
                })
                .toList();
        return new OrderProductListResponse(orderProductResponses);
    }

    /**
     * 재고 감소
     * @param request
     */
    public void decreaseProductsStock(DecreaseStockRequest request) {
        List<ProductUnitCountRequest> list = new ArrayList<>();

        try {
            request.getOrderProducts().forEach(productUnit -> {
                list.add(productStockService.decreaseStock(productUnit));
            });
        } catch (BusinessException e) {
            log.error("BusinessException", e);
            productProducer.rollbackCreatedOrder(new OrderRollbackDto(request.getOrderId(), list));
        }
    }

//    @CircuitBreaker(name = "order-service", fallbackMethod = "decreaseProductsStockFallback")
//    @Retry(name = "order-service")
//    public void decreaseProductsStock(DecreaseStockRequest request) {
//        request.getOrderProducts().forEach(productStockService::decreaseStock);
//
//        // 결제 진입
//        paymentClient.processPayment(new ProcessPaymentRequest(request.getUserId(), request.getOrderId(), request.getOrderProducts()));
//    }

    public void decreaseProductsStockFallback(DecreaseStockRequest request, BusinessException e) {
        throw new BusinessException(e.getExceptionCode());
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductForWishlist(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

        return ProductDetailResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .build();
    }

    @Transactional
    public void restoreStock(RestoreStockRequest request) {
        request.getOrderProducts().forEach(productStockService::restoreStock);
    }

    @Transactional(readOnly = true)
    public ProductStockResponse getProductStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_PRODUCT));

        return new ProductStockResponse(product.getId(), product.getStock());
    }

    @Transactional
    public void addProduct(AddProductRequest addProductRequest) {
        Product product = Product.builder()
                .name(addProductRequest.getName())
                .description(addProductRequest.getDescription())
                .stock(addProductRequest.getStock())
                .price(addProductRequest.getPrice())
                .producer(addProductRequest.getProducer())
                .build();

        productRepository.save(product);
    }
}
