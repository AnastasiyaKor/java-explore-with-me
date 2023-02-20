package ru.practicum.stats_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats_dto.*;
import ru.practicum.stats_service.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("SELECT new ru.practicum.stats_dto.ViewStats(e.app, e.uri, count(e.ip))" +
            "from EndpointHit as e where e.timestamp between ?1 and ?2 and e.uri in (?3) group by e.app, e.uri " +
            "order by count (e.ip)DESC ")
    List<ViewStats> countHit(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("SELECT new ru.practicum.stats_dto.ViewStats(" +
            "e.app, e.uri, count(distinct e.ip))" +
            "from EndpointHit as e where e.timestamp between ?1 and ?2 and e.uri in (?3) group by e.app, e.uri " +
            "order by count (e.ip)DESC ")
    List<ViewStats> countDistinctHit(LocalDateTime start, LocalDateTime end, List<String> uri);


}
