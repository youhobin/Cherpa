package com.cerpha.orderservice.common.event.handler;

import com.cerpha.orderservice.cerpha.order.request.ProcessPaymentRequest;
import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.client.product.response.OrderProductDetailResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class OrderEventHandler {

    @Value("${env.kafka.producer.topic.stock-decrease}")
    private String decreaseStockTopic;

    @Value("${env.kafka.producer.topic.process-payment}")
    private String processPaymentTopic;

    @Value("${env.kafka.producer.topic.stock-restore}")
    private String restoreStockTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventHandler(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @TransactionalEventListener(classes = DecreaseStockRequest.class, phase = TransactionPhase.BEFORE_COMMIT)
    @Async
    public void handle(DecreaseStockRequest decreaseStockRequest) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(decreaseStockRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(decreaseStockTopic, jsonString);
        log.info("Kafka OrderProducer send Data for Decrease Stock: " + jsonString);
    }

    @TransactionalEventListener(classes = ProcessPaymentRequest.class, phase = TransactionPhase.BEFORE_COMMIT)
    @Async
    public void handle(ProcessPaymentRequest processPaymentRequest) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(processPaymentRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(processPaymentTopic, jsonString);
        log.info("Kafka OrderProducer send Data for Process Payment: " + jsonString);
    }

    @TransactionalEventListener(classes = RestoreStockRequest.class, phase = TransactionPhase.BEFORE_COMMIT)
    @Async
    public void handle(RestoreStockRequest restoreStockRequest) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(restoreStockRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(restoreStockTopic, jsonString);
        log.info("Kafka OrderProducer send Data for Restore Stock: " + restoreStockRequest);
    }

}
