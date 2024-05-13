package com.cerpha.orderservice.common.event.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "order_events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String event;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private LocalDateTime createdAt;

    private boolean published;

    public Event(String event, EventType eventType) {
        this.event = event;
        this.type = eventType;
        this.createdAt = LocalDateTime.now();
        this.published = false;
    }
}
