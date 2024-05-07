package com.cerpha.paymentservice.cerpha.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentProducer {

    @Value("${env.kafka.producer.topic.rollback-created-order}")
    private String orderRollbackTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void rollbackCreatedOrder(Long orderId) {
        kafkaTemplate.send(orderRollbackTopic, String.valueOf(orderId));
        log.info("Kafka PaymentProducer send Data for rollback created order: " + orderId);
    }
}
