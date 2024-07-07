package com.example.hpbackend.controller;

import com.example.hpbackend.entity.ChatMessage;
import com.example.hpbackend.entity.User;
import com.example.hpbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private UserRepository userService;
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        return "Hello, " + message + "!";
    }
/*    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ) {
      Optional<User> user = Optional.ofNullable(userService.findByUsername(chatMessage.getSender().getUsername()));
        // Optional<User> user2 = Optional.ofNullable(userService.findByUser(chatMessage.getSender()));
        user.ifPresent(chatMessage::setSender);
        return chatMessage;
    }*/


}
