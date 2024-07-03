package com.example.hpbackend.config;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Component
public class ImageUploader {

    private final ResourceLoader resourceLoader;

    public ImageUploader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public byte[] uploadImage() {
        try {
            Path imagePath = Path.of(resourceLoader.getResource("classpath:static/images/profile.jpeg").getURI());
            System.out.println(Arrays.toString(Files.readAllBytes(imagePath)));
            return Files.readAllBytes(imagePath);

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
