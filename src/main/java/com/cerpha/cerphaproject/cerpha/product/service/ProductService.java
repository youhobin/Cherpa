package com.cerpha.cerphaproject.cerpha.product.service;

import com.cerpha.cerphaproject.cerpha.product.domain.Products;
import com.cerpha.cerphaproject.cerpha.product.repository.ProductRepository;
import com.cerpha.cerphaproject.cerpha.product.response.ProductListResponse;
import com.cerpha.cerphaproject.common.dto.PageResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ProductListResponse> getAllProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Products> productsPage = productRepository.findAll(pageRequest);
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
}
