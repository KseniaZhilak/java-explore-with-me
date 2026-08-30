package ru.practicum.main.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<RequestsEntity, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    Collection<RequestsEntity> findAllByUserId(Long userId);

    Optional<RequestsEntity> findByIdAndUserId(Long id, Long userId);
}
