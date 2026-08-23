package ru.practicum.main.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {

    Boolean existsByNameEqualsIgnoreCase(String name);

    boolean existsById(Long id);

}
