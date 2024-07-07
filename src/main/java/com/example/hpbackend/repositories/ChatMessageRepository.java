package com.example.hpbackend.repositories;
import com.example.hpbackend.entity.ChatMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends ListCrudRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m ORDER BY m.createdAt DESC")
    List<ChatMessage> findAllSortedByCreatedAtDesc();
}
