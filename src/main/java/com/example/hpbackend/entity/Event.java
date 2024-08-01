package com.example.hpbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    private String content;

    @CreationTimestamp
    private LocalDateTime startTime;

 /*   @ManyToMany(mappedBy = "attendingEvents", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<User> attendees;
*/
 @Setter
 @Getter
 private Long attendees;

    public Event(Long id, String title, String content, Long attendees) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.attendees = attendees;
    }

    public Event(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Event() {
    }
}
