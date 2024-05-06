package com.cerpha.orderservice.cerpha.order.service;

import com.cerpha.orderservice.cerpha.order.request.ProcessPaymentRequest;
import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProducer {

    @Value("${env.kafka.producer.topic.stock-decrease}")
    private String decreaseStockTopic;

    @Value("${env.kafka.producer.topic.process-payment}")
    private String processPaymentTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void decreaseStock(DecreaseStockRequest decreaseStockRequest) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(decreaseStockRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(decreaseStockTopic, jsonString);
        log.info("Kafka OrderProducer send Data for Decrease Stock: " + decreaseStockRequest);
    }

    public void processPayment(ProcessPaymentRequest processPaymentRequest) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(processPaymentRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(processPaymentTopic, jsonString);
        log.info("Kafka OrderProducer send Data for Process Payment: " + processPaymentRequest);
    }
}
