package ru.practicum.main.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<EventsEntity, Long> {

    boolean existsByCategoryId(Long categoryId);

    @Query(value = """
            SELECT * FROM events
            WHERE user_id = :userId
            ORDER BY id
            LIMIT :size OFFSET :offset
            """, nativeQuery = true)
    Collection<EventsEntity> findAllByUserId(Long userId, int offset, int size);

    @Query(value = """
            SELECT * FROM events
            WHERE id = :id AND user_id = :userId
            """, nativeQuery = true)
    Optional<EventsEntity> findByIdAndUserId(Long id, Long userId);

    @Query(value = """
            SELECT * FROM events
            WHERE (:filterByUsers = false OR user_id IN :users)
              AND (:filterByStates = false OR state IN :states)
              AND (:filterByCategories = false OR category_id IN :categories)
              AND (CAST(:rangeStart AS TIMESTAMP) IS NULL OR event_date >= :rangeStart)
              AND (CAST(:rangeEnd AS TIMESTAMP) IS NULL OR event_date <= :rangeEnd)
            ORDER BY id
            LIMIT :size OFFSET :offset
            """, nativeQuery = true)
    Collection<EventsEntity> findEventsByFilters(List<Long> users, boolean filterByUsers,
                                                 List<String> states, boolean filterByStates,
                                                 List<Long> categories, boolean filterByCategories,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 int offset, int size);

    @Query(value = """
            SELECT * FROM events
            WHERE state = 'PUBLISHED'
              AND (CAST(:text AS TEXT) IS NULL
                   OR annotation ILIKE CONCAT('%', CAST(:text AS TEXT), '%')
                   OR description ILIKE CONCAT('%', CAST(:text AS TEXT), '%'))
              AND (:filterByCategories = false OR category_id IN :categories)
              AND (CAST(:paid AS BOOLEAN) IS NULL OR paid = CAST(:paid AS BOOLEAN))
              AND (CAST(:rangeStart AS TIMESTAMP) IS NULL OR event_date >= :rangeStart)
              AND (CAST(:rangeEnd AS TIMESTAMP) IS NULL OR event_date <= :rangeEnd)
              AND (CAST(:rangeStart AS TIMESTAMP) IS NOT NULL
                   OR CAST(:rangeEnd AS TIMESTAMP) IS NOT NULL
                   OR event_date > CURRENT_TIMESTAMP)
              AND (:onlyAvailable = false
                   OR COALESCE(participant_limit, 0) = 0
                   OR COALESCE(confirmed_requests, 0) < participant_limit)
            ORDER BY event_date, id
            LIMIT :size OFFSET :offset
            """, nativeQuery = true)
    Collection<EventsEntity> findPublishedEventsByFilters(String text,
                                                          List<Long> categories, boolean filterByCategories,
                                                          Boolean paid,
                                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                          boolean onlyAvailable,
                                                          int offset, int size);
}
