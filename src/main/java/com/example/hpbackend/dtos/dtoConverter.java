package com.example.hpbackend.dtos;
import com.example.hpbackend.entity.ChatMessage;
import com.example.hpbackend.entity.User;

public class dtoConverter {

    public static ChatUserDTO convertToUserDTO(User user) {
        ChatUserDTO userDTO = new ChatUserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setImageData(user.getImageData() != null ? new String(user.getImageData()) : null);
        return userDTO;
    }

    public static ChatMessageDto convertToChatMessageDTO(ChatMessage chatMessage) {
        ChatMessageDto chatMessageDTO = new ChatMessageDto();
        chatMessageDTO.setId(chatMessage.getId());
        chatMessageDTO.setContent(chatMessage.getContent());
        chatMessageDTO.setCreatedAt(chatMessage.getCreatedAt());
        chatMessageDTO.setSender(convertToUserDTO(chatMessage.getSender()));
        return chatMessageDTO;
    }
}
