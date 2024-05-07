package com.cerpha.orderservice.cerpha.order.service;

import com.cerpha.orderservice.cerpha.order.request.OrderRollbackRequest;
import com.cerpha.orderservice.common.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.cerpha.orderservice.common.exception.ExceptionCode.CHANGE_MIND;

@Service
@Slf4j
public class OrderConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderConsumer(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${env.kafka.consumer.topic.rollback-created-order}")
    public void rollbackCreatedOrder(String message) {
        log.info("kafka listener rollback created order message ={}", message);
        OrderRollbackRequest orderRollbackRequest = null;
        try {
            orderRollbackRequest = objectMapper.readValue(message, OrderRollbackRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            orderService.rollbackCreatedOrder(orderRollbackRequest);
        } catch (BusinessException e) {
            log.error("BusinessException", e);
        }
    }
}
