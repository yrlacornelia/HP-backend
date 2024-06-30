package com.example.hpbackend.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUserForm {
    @NotNull
    private Long id;


    @Getter
    @Size(min = 1, max = 100)
    private String username;

    public EditUserForm(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
