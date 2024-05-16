package com.cerpha.orderservice.common.event.scheduler;

import com.cerpha.orderservice.common.event.domain.Event;
import com.cerpha.orderservice.common.event.dto.OutboxDecreaseStockDto;
import com.cerpha.orderservice.common.event.dto.OutboxProcessPaymentDto;
import com.cerpha.orderservice.common.event.dto.OutboxRestoreStockDto;
import com.cerpha.orderservice.common.event.producer.OutboxEventProducer;
import com.cerpha.orderservice.common.event.service.OutboxEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.cerpha.orderservice.common.event.domain.EventType.*;

@Component
@Slf4j
@EnableScheduling
public class EventScheduler {

    private final OutboxEventService outboxEventService;
    private final OutboxEventProducer outboxEventProducer;

    public EventScheduler(OutboxEventService outboxEventService, OutboxEventProducer outboxEventProducer) {
        this.outboxEventService = outboxEventService;
        this.outboxEventProducer = outboxEventProducer;
    }

    @Scheduled(cron = "0 * * * * *")
    public void republishEvent() {
        log.info("이벤트 재발행");
        List<Event> notPublishedEvent = outboxEventService.findAllNotPublishedEvent();
        notPublishedEvent.forEach(event -> {
            if (event.getType().equals(DECREASE_STOCK)) {
                outboxEventProducer.produceEvent(new OutboxDecreaseStockDto(event.getId(), event.getEvent()));
            } else if (event.getType().equals(PROCESS_PAYMENT)) {
                outboxEventProducer.produceEvent(new OutboxProcessPaymentDto(event.getId(), event.getEvent()));
            } else if (event.getType().equals(RESTORE_STOCK)) {
                outboxEventProducer.produceEvent(new OutboxRestoreStockDto(event.getId(), event.getEvent()));
            }
        });
    }
}
