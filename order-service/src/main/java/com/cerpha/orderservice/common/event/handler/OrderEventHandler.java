package com.cerpha.orderservice.common.event.handler;

import com.cerpha.orderservice.cerpha.order.request.ProcessPaymentRequest;
import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.event.service.OutboxEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final OutboxEventService outboxEventService;

    public OrderEventHandler(OutboxEventService outboxEventService) {
        this.outboxEventService = outboxEventService;
    }

    @TransactionalEventListener(classes = DecreaseStockRequest.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(DecreaseStockRequest decreaseStockRequest) {
        outboxEventService.saveDecreaseStock(decreaseStockRequest);
    }

    @TransactionalEventListener(classes = ProcessPaymentRequest.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(ProcessPaymentRequest processPaymentRequest) {
        outboxEventService.saveProcessPayment(processPaymentRequest);
    }

    @TransactionalEventListener(classes = RestoreStockRequest.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(RestoreStockRequest restoreStockRequest) {
        outboxEventService.saveRestoreStock(restoreStockRequest);
    }

}
