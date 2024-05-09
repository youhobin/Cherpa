package com.cerpha.paymentservice.cerpha.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentProducer {

    @Value("${env.kafka.producer.topic.cancel-created-order}")
    private String orderCancelTopic;

    @Value("${env.kafka.producer.topic.complete-payment}")
    private String completePaymentTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void cancelCreatedOrder(Long orderId) {
        kafkaTemplate.send(orderCancelTopic, String.valueOf(orderId));
        log.info("Kafka PaymentProducer send Data for rollback created order: " + orderId);
    }

    public void completePayment(Long orderId) {
        kafkaTemplate.send(completePaymentTopic, String.valueOf(orderId));
        log.info("Kafka PaymentProducer send Data for complete payment: " + orderId);
    }
}
