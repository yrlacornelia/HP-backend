package com.example.hpbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private LocalDateTime startTime;

    @ManyToMany(mappedBy = "attendingEvents")
    Set<User> attendees;

    public Event(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Event() {
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setAttendees(Set<User> attendees) {
        this.attendees = attendees;
    }
}
