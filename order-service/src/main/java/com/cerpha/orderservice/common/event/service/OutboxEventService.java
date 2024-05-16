package com.cerpha.orderservice.common.event.service;

import com.cerpha.orderservice.cerpha.order.request.ProcessPaymentRequest;
import com.cerpha.orderservice.common.client.product.request.DecreaseStockRequest;
import com.cerpha.orderservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.orderservice.common.event.domain.Event;
import com.cerpha.orderservice.common.event.dto.OutboxDecreaseStockDto;
import com.cerpha.orderservice.common.event.dto.OutboxProcessPaymentDto;
import com.cerpha.orderservice.common.event.dto.OutboxRestoreStockDto;
import com.cerpha.orderservice.common.event.producer.OutboxEventProducer;
import com.cerpha.orderservice.common.event.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.cerpha.orderservice.common.event.domain.EventType.*;

@Service
public class OutboxEventService {

    private final EventRepository eventRepository;
    private final OutboxEventProducer outboxEventProducer;
    private final ObjectMapper objectMapper;

    public OutboxEventService(EventRepository eventRepository, OutboxEventProducer outboxEventProducer, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.outboxEventProducer = outboxEventProducer;
        this.objectMapper = objectMapper;
    }

    public void updatePublished(Long id) {
        eventRepository.updatePublished(id);
    }

    public void saveDecreaseStock(DecreaseStockRequest decreaseStockRequest) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(decreaseStockRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Event event = new Event(jsonString, DECREASE_STOCK);
        Event outboxEvent = eventRepository.save(event);
        outboxEventProducer.produceEvent(new OutboxDecreaseStockDto(outboxEvent.getId(), jsonString));
    }

    public void saveProcessPayment(ProcessPaymentRequest processPaymentRequest) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(processPaymentRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Event event = new Event(jsonString, PROCESS_PAYMENT);
        Event outboxEvent = eventRepository.save(event);
        outboxEventProducer.produceEvent(new OutboxProcessPaymentDto(outboxEvent.getId(), jsonString));
    }

    public void saveRestoreStock(RestoreStockRequest restoreStockRequest) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(restoreStockRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Event event = new Event(jsonString, RESTORE_STOCK);
        Event outboxEvent = eventRepository.save(event);
        outboxEventProducer.produceEvent(new OutboxRestoreStockDto(outboxEvent.getId(), jsonString));
    }

    public List<Event> findAllNotPublishedEvent() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(2);
        return eventRepository.findAllNotPublishedEvent(time);
    }
}
