package ru.practicum.main.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<CommentsEntity, Long> {

    Optional<CommentsEntity> findByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT * FROM comments WHERE event_id = :eventId " +
            "ORDER BY created LIMIT :size OFFSET :offset", nativeQuery = true)
    Collection<CommentsEntity> findAllByEventId(@Param("eventId") Long eventId,
                                                @Param("offset") int offset,
                                                @Param("size") int size);

}
