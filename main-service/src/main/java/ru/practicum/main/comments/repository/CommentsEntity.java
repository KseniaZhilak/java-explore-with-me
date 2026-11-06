package ru.practicum.main.comments.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.events.repository.EventsEntity;
import ru.practicum.main.users.repository.UsersEntity;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentsEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String text;

    private LocalDateTime created;

    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventsEntity event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

}
