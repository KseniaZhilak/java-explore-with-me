package ru.practicum.main.events.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.categories.repository.CategoriesEntity;
import ru.practicum.main.events.states.State;
import ru.practicum.main.users.repository.UsersEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoriesEntity category;

    @Column(name = "confirmed_requests", nullable = false)
    private Long confirmedRequests = 0L;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    private Double lat;

    private Double lon;

    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

}
