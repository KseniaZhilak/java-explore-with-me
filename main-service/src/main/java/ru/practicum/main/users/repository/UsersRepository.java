package ru.practicum.main.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    Boolean existsByEmailEqualsIgnoreCase(String email);

    @Query(value = "SELECT * FROM users " +
            "WHERE (:filterByIds = false or id in :ids) " +
            "ORDER BY id LIMIT :size OFFSET :offset", nativeQuery = true)
    Collection<UsersEntity> findUsersByFilters(List<Integer> ids, boolean filterByIds, int offset, int size);

}
