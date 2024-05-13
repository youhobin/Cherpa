package com.cerpha.orderservice.common.event.repository;

import com.cerpha.orderservice.common.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Modifying
    @Query("UPDATE Event e SET e.published = true WHERE e.id = :id")
    void updatePublished(Long id);
}
