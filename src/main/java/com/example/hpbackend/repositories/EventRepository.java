package com.example.hpbackend.repositories;

import com.example.hpbackend.entity.Event;
import org.springframework.data.repository.ListCrudRepository;

public interface EventRepository extends ListCrudRepository<Event, Long> {

    Object findAllById(int i);
    Object findById(int i);
}
