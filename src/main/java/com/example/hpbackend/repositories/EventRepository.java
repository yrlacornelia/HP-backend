package com.example.hpbackend.repositories;

import com.example.hpbackend.entity.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends ListCrudRepository<Event, Long> {

    Object findAllById(int i);
    Object findById(int i);
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.attendees WHERE e.id = :eventId")
    Optional<Event> findByIdWithAttendees(@Param("eventId") Long eventId);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.attendees")
    List<Event> findAllWithAttendees();
}
