package com.cerpha.productservice.cerpha.product.service;

import com.cerpha.productservice.cerpha.product.request.OrderRollbackRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public ProductProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void rollbackCreatedOrder(Long orderId) {
        OrderRollbackRequest orderRollbackRequest = new OrderRollbackRequest(orderId, false);
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(orderRollbackRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 주문 롤백
        kafkaTemplate.send(orderRollbackTopic, jsonString);
        // 결제 롤백
        kafkaTemplate.send(paymentRollbackTopic, String.valueOf(orderId));
        log.info("Kafka PaymentProducer send Data for rollback created order: " + orderId);
    }
}
