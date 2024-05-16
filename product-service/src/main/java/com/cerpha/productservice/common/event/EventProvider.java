package com.cerpha.productservice.common.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventProvider {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventProvider(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void produceEvent(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
