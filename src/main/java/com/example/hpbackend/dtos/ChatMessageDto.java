package com.example.hpbackend.dtos;



import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private ChatUserDTO sender;
}
