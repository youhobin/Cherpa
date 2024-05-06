package com.cerpha.paymentservice.cerpha.payment.service;

import com.cerpha.paymentservice.cerpha.payment.request.ProcessPaymentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentConsumer {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    public PaymentConsumer(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${env.kafka.consumer.topic.process-payment}")
    public void processPayment(String message) {
        log.info("kafka listener process payment message ={}", message);
        ProcessPaymentRequest processPaymentRequest = null;
        try {
            processPaymentRequest = objectMapper.readValue(message, ProcessPaymentRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        paymentService.processPayment(processPaymentRequest);
    }

}
