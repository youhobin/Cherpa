package com.cerpha.orderservice.common.event.repository;

import com.cerpha.orderservice.common.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Modifying
    @Query("UPDATE Event e SET e.published = true WHERE e.id = :id")
    void updatePublished(Long id);

    @Query("SELECT e FROM Event e WHERE e.published = false AND e.createdAt > :time")
    List<Event> findAllNotPublishedEvent(LocalDateTime time);
}
