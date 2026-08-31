package ru.practicum.main.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.requests.StatusRequest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<RequestsEntity, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    Collection<RequestsEntity> findAllByUserId(Long userId);

    Collection<RequestsEntity> findAllByEventId(Long eventId);

    List<RequestsEntity> findAllByIdInAndEventId(Collection<Long> ids, Long eventId);

    List<RequestsEntity> findAllByEventIdAndStatus(Long eventId, StatusRequest status);

    Optional<RequestsEntity> findByIdAndUserId(Long id, Long userId);
}
