package com.example.hpbackend.repositories;

import com.example.hpbackend.entity.House;
import org.springframework.data.repository.ListCrudRepository;

public interface HouseRepository extends ListCrudRepository<House, Long> {
}

