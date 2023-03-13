package ru.practicum.stats_service.service;

import ru.practicum.stats_dto.ViewStats;
import ru.practicum.stats_service.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHit addHit(EndpointHit endpointHit);

    List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
