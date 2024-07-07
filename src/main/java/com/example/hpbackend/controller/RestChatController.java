package com.example.hpbackend.controller;

import com.example.hpbackend.entity.ChatMessage;
import com.example.hpbackend.repositories.ChatMessageRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("chat")
public class RestChatController {

    private final ChatMessageRepository chatMessageRepository;

    public RestChatController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }



    // REST endpoint to fetch all chat messages
    @GetMapping("/returnAllMessages")
    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAllSortedByCreatedAtDesc();
    }
}