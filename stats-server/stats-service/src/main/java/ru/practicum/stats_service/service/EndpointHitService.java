package ru.practicum.stats_service.service;

import ru.practicum.stats_dto.*;
import ru.practicum.stats_service.model.EndpointHit;

import java.util.List;

public interface EndpointHitService {
    EndpointHit create(EndpointHit endpointHit);

    List<ViewStats> get(String start, String end, List<String> uris, boolean unique);
}
