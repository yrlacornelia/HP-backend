package com.example.hpbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    private String content;

    @CreationTimestamp
    private LocalDateTime startTime;

    @ManyToMany(mappedBy = "attendingEvents", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("attendingEvents") // Ignore the back reference to prevent infinite recursion
    private List<User> attendees = new ArrayList<>();

    public Event(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Event() {
    }
}
