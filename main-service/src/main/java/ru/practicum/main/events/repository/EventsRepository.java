package ru.practicum.main.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<EventsEntity, Long> {

    @Query(value = "SELECT * FROM events WHERE user_id = :userId ORDER BY id LIMIT :size OFFSET :offset", nativeQuery = true)
    Collection<EventsEntity> findAllByUserId(Long userId, int offset, int size);

    @Query(value = "SELECT * FROM events WHERE id = :id AND user_id = :userId", nativeQuery = true)
    Optional<EventsEntity> findByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT * FROM events " +
            "WHERE (:filterByUsers = false or user_id in :users) " +
            "AND (:filterByStates = false or state in :states) " +
            "AND (:filterByCategories = false or category_id in :categories) " +
            "AND (CAST(:rangeStart AS TIMESTAMP) is null or event_date >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS TIMESTAMP) is null or event_date <= :rangeEnd) " +
            "ORDER BY id LIMIT :size OFFSET :offset", nativeQuery = true)
    Collection<EventsEntity> findEventsByFilters(List<Long> users, boolean filterByUsers,
                                                 List<String> states, boolean filterByStates,
                                                 List<Long> categories, boolean filterByCategories,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 int offset, int size);

}
