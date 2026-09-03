package ru.practicum.main.requests.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.events.repository.EventsEntity;
import ru.practicum.main.requests.StatusRequest;
import ru.practicum.main.users.repository.UsersEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private StatusRequest status;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventsEntity event;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

}
