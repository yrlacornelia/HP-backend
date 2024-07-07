package com.example.hpbackend.controller;


import com.example.hpbackend.entity.User;
import com.example.hpbackend.repositories.HouseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/house")
@CrossOrigin(origins = "http://localhost:3000")
public class HouseController {
    private final HouseRepository houseRepository;

    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }


    // all slytherin ex


}
