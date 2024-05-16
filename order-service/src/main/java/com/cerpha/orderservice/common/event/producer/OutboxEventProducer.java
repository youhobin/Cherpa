package com.cerpha.orderservice.common.event.producer;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventProducer {

    private final ApplicationEventPublisher eventPublisher;

    public OutboxEventProducer(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void produceEvent(Object object) {
        eventPublisher.publishEvent(object);
    }
}
