package com.example.hpbackend.dtos;
// UserDTO.java

import lombok.Data;

@Data
public class ChatUserDTO {
    private Long id;
    private String username;
    private String imageData; // Consider using a different type if imageData should be included
}
