package com.cerpha.paymentservice.common.event.handler;

import com.cerpha.paymentservice.common.event.OrderCancelEvent;
import com.cerpha.paymentservice.common.event.PaymentCompleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class PaymentEventHandler {

    @Value("${env.kafka.producer.topic.cancel-created-order}")
    private String orderCancelTopic;

    @Value("${env.kafka.producer.topic.complete-payment}")
    private String completePaymentTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentEventHandler(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(classes = OrderCancelEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    @Async
    public void handle(OrderCancelEvent orderCancelEvent) {
        kafkaTemplate.send(orderCancelTopic, String.valueOf(orderCancelEvent.getOrderId()));
        log.info("Kafka PaymentProducer send Data for rollback created order: " + orderCancelEvent.getOrderId());
    }

    @TransactionalEventListener(classes = PaymentCompleteEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    @Async
    public void handle(PaymentCompleteEvent paymentCompleteEvent) {
        kafkaTemplate.send(completePaymentTopic, String.valueOf(paymentCompleteEvent.getOrderId()));
        log.info("Kafka PaymentProducer send Data for complete payment: " + paymentCompleteEvent.getOrderId());
    }
}
