package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface HitRepository extends JpaRepository<HitEntity, Long> {

    @Query("""
            select new ru.practicum.stat.dto.ViewStatsDto(h.app, h.uri, count(h.ip))
            from HitEntity h
            where h.timestamp between :start and :end
                        and (:filterByUris = false or h.uri in :uris)
            group by h.app, h.uri
            order by count(h.ip) desc
            """)
    List<ViewStatsDto> findStats(@Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end,
                                 @Param("uris") Collection<String> uris,
                                 @Param("filterByUris") Boolean filterByUris);

    @Query("""
            select new ru.practicum.stat.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip))
            from HitEntity h
            where h.timestamp between :start and :end
                        and (:filterByUris = false or h.uri in :uris)
            group by h.app, h.uri
            order by count(distinct h.ip) desc
            """)
    List<ViewStatsDto> findUniqueStats(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("uris") Collection<String> uris,
                                       @Param("filterByUris") Boolean filterByUris);

}
