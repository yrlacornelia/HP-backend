package com.example.hpbackend.controller;

import com.example.hpbackend.dtos.ChatMessageDto;
import com.example.hpbackend.dtos.dtoConverter;
import com.example.hpbackend.entity.ChatMessage;
import com.example.hpbackend.entity.User;
import com.example.hpbackend.repositories.ChatMessageRepository;
import com.example.hpbackend.repositories.UserRepository;
import com.example.hpbackend.services.AuthService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ChatController {

    private final UserRepository userService;


    private final ChatMessageRepository chatMessageRepository;

    private final AuthService authService;

    public ChatController(UserRepository userService, ChatMessageRepository chatMessageRepository, AuthService authService) {
        this.userService = userService;
        this.chatMessageRepository = chatMessageRepository;
        this.authService = authService;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        return "Hello, " + message + "!";
    }
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(
            @Payload ChatMessage chatMessage
    ) {
        Optional<User> user = Optional.ofNullable(userService.findByUsername(chatMessage.getSender().getUsername()));
        user.ifPresent(chatMessage::setSender);
        if (chatMessage.getCreatedAt() == null) {
            chatMessage.setCreatedAt(LocalDateTime.now());
        }

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        return dtoConverter.convertToChatMessageDTO(savedMessage);
    }


}
