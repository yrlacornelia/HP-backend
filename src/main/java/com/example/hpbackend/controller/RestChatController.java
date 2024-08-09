package com.example.hpbackend.controller;

import com.example.hpbackend.dtos.ChatMessageDto;
import com.example.hpbackend.dtos.dtoConverter;
import com.example.hpbackend.entity.ChatMessage;
import com.example.hpbackend.repositories.ChatMessageRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("chat")
public class RestChatController {

    private final ChatMessageRepository chatMessageRepository;

    public RestChatController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/returnAllMessages")
    public List<ChatMessageDto> getAllMessages() {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllSortedByCreatedAtDesc();
        return chatMessages.stream()
                .map(dtoConverter::convertToChatMessageDTO)
                .collect(Collectors.toList());
    }}