package com.cerpha.productservice.cerpha.product.service;

import com.cerpha.productservice.cerpha.product.request.OrderRollbackDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductProducer {

    @Value("${env.kafka.producer.topic.rollback-created-order}")
    private String orderRollbackTopic;

    @Value("${env.kafka.producer.topic.rollback-payment}")
    private String paymentRollbackTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProductStockService productStockService;

    public ProductProducer(KafkaTemplate<String, String> kafkaTemplate, ProductStockService productStockService) {
        this.kafkaTemplate = kafkaTemplate;
        this.productStockService = productStockService;
    }

    public void rollbackCreatedOrder(OrderRollbackDto orderRollbackDto) {
        // 재고 롤백
        orderRollbackDto.getOrderProducts().forEach(productStockService::restoreStock);
        // 주문 롤백
        kafkaTemplate.send(orderRollbackTopic, String.valueOf(orderRollbackDto.getOrderId()));
        // 결제 롤백
        kafkaTemplate.send(paymentRollbackTopic, String.valueOf(orderRollbackDto.getOrderId()));
        log.info("Kafka PaymentProducer send Data for rollback created order: " + orderRollbackDto.getOrderId());
    }
}
