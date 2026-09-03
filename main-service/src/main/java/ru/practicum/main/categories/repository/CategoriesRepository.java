package ru.practicum.main.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {

    Boolean existsByNameEqualsIgnoreCase(String name);

    Boolean existsByNameEqualsIgnoreCaseAndIdNot(String name, Long id);

    @Query(value = "SELECT * FROM categories ORDER BY id LIMIT :size OFFSET :offset", nativeQuery = true)
    List<CategoriesEntity> findAll(@Param("offset") int offset, @Param("size") int size);

}
