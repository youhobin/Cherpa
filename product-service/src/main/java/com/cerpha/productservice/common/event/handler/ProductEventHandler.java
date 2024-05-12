package com.cerpha.productservice.common.event.handler;

import com.cerpha.productservice.cerpha.product.request.OrderRollbackDto;
import com.cerpha.productservice.cerpha.product.service.ProductStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ProductEventHandler {

    @Value("${env.kafka.producer.topic.rollback-created-order}")
    private String orderRollbackTopic;

    @Value("${env.kafka.producer.topic.rollback-payment}")
    private String paymentRollbackTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProductStockService productStockService;

    public ProductEventHandler(KafkaTemplate<String, String> kafkaTemplate, ProductStockService productStockService) {
        this.kafkaTemplate = kafkaTemplate;
        this.productStockService = productStockService;
    }

    @EventListener(classes = OrderRollbackDto.class)
    @Async
    public void handle(OrderRollbackDto orderRollbackDto) {
        // 재고 롤백
        orderRollbackDto.getOrderProducts().forEach(productStockService::restoreStock);
        // 주문 롤백
        kafkaTemplate.send(orderRollbackTopic, String.valueOf(orderRollbackDto.getOrderId()));
        // 결제 롤백
        kafkaTemplate.send(paymentRollbackTopic, String.valueOf(orderRollbackDto.getOrderId()));
        log.info("Kafka PaymentProducer send Data for rollback created order: " + orderRollbackDto.getOrderId());
    }
}
