package com.example.hpbackend.repositories;

import com.example.hpbackend.entity.User;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, Long> {

}
