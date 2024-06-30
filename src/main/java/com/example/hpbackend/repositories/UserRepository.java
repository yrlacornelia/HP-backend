package com.example.hpbackend.repositories;

import com.example.hpbackend.entity.House;
import com.example.hpbackend.entity.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface UserRepository extends ListCrudRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByHouseName(String houseName);

    String findByHouseId(House house);
}
