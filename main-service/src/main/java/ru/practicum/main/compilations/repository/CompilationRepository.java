package ru.practicum.main.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompilationRepository extends JpaRepository<CompilationEntity, Long> {

    @Query(value = "SELECT * FROM compilations " +
            "WHERE (:filterByPinned = false or pinned = :pinned) " +
            "ORDER BY id LIMIT :size OFFSET :offset", nativeQuery = true)
    List<CompilationEntity> findCompilationsByFilters(@Param("filterByPinned") boolean filterByPinned,
                                                      @Param("pinned") boolean pinned,
                                                      @Param("offset") int offset,
                                                      @Param("size") int size);

}
