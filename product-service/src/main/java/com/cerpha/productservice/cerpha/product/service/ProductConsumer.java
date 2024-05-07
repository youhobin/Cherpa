package com.cerpha.productservice.cerpha.product.service;

import com.cerpha.productservice.cerpha.product.request.DecreaseStockRequest;
import com.cerpha.productservice.cerpha.product.request.RestoreStockRequest;
import com.cerpha.productservice.common.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductConsumer {

    private final ProductProducer productProducer;
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public ProductConsumer(ProductProducer productProducer, ProductService productService, ObjectMapper objectMapper) {
        this.productProducer = productProducer;
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${env.kafka.consumer.topic.stock-decrease}")
    public void decreaseStock(String message) {
        log.info("kafka listener decrease stock message ={}", message);
        DecreaseStockRequest decreaseStockRequest = null;
        try {
            decreaseStockRequest = objectMapper.readValue(message, DecreaseStockRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            productService.decreaseProductsStock(decreaseStockRequest);
        } catch (BusinessException e) {
            log.error("BusinessException", e);
            productProducer.rollbackCreatedOrder(decreaseStockRequest.getOrderId());
        }
    }

    @KafkaListener(topics = "${env.kafka.consumer.topic.stock-restore}")
    public void restoreStock(String message) {
        log.info("kafka listener restore stock message ={}", message);
        RestoreStockRequest restoreStockRequest = null;
        try {
            restoreStockRequest = objectMapper.readValue(message, RestoreStockRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        productService.restoreStock(restoreStockRequest);
    }
}
