package com.cerpha.orderservice.common.event.consumer;

import com.cerpha.orderservice.common.event.domain.Event;
import com.cerpha.orderservice.common.event.dto.OutboxDecreaseStockDto;
import com.cerpha.orderservice.common.event.dto.OutboxProcessPaymentDto;
import com.cerpha.orderservice.common.event.dto.OutboxRestoreStockDto;
import com.cerpha.orderservice.common.event.service.OutboxEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.cerpha.orderservice.common.config.AsyncConfig.EVENT_EXECUTOR;

@Component
@Slf4j
public class OutboxEventConsumer {

    @Value("${env.kafka.producer.topic.stock-decrease}")
    private String decreaseStockTopic;

    @Value("${env.kafka.producer.topic.process-payment}")
    private String processPaymentTopic;

    @Value("${env.kafka.producer.topic.stock-restore}")
    private String restoreStockTopic;

    private final OutboxEventService outboxEventService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxEventConsumer(OutboxEventService outboxEventService, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxEventService = outboxEventService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async(EVENT_EXECUTOR)
    @TransactionalEventListener(classes = OutboxDecreaseStockDto.class, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OutboxDecreaseStockDto dto) {
        outboxEventService.updatePublished(dto.getId());
        kafkaTemplate.send(decreaseStockTopic, dto.getEvent());
        log.info("Kafka OrderProducer send Data for Decrease Stock: " + dto.getEvent());
    }

    @Async(EVENT_EXECUTOR)
    @TransactionalEventListener(classes = OutboxProcessPaymentDto.class, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OutboxProcessPaymentDto dto) {
        outboxEventService.updatePublished(dto.getId());
        kafkaTemplate.send(processPaymentTopic, dto.getEvent());
        log.info("Kafka OrderProducer send Data for Process Payment: " + dto.getEvent());
    }

    @Async(EVENT_EXECUTOR)
    @TransactionalEventListener(classes = OutboxRestoreStockDto.class, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OutboxRestoreStockDto dto) {
        outboxEventService.updatePublished(dto.getId());
        kafkaTemplate.send(restoreStockTopic, dto.getEvent());
        log.info("Kafka OrderProducer send Data for Restore Stock: " + dto.getEvent());
    }
}
