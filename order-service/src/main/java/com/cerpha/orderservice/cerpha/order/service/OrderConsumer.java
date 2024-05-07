package com.cerpha.orderservice.cerpha.order.service;

import com.cerpha.orderservice.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.cerpha.orderservice.common.exception.ExceptionCode.CHANGE_MIND;

@Service
@Slf4j
public class OrderConsumer {

    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${env.kafka.consumer.topic.rollback-created-order}")
    public void rollbackCreatedOrder(String message) {
        log.info("kafka listener rollback created order message ={}", message);

        orderService.rollbackCreatedOrder(Long.valueOf(message));
    }
}
