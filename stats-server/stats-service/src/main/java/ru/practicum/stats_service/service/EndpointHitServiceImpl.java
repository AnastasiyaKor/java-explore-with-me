package ru.practicum.stats_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats_dto.ViewStats;
import ru.practicum.stats_service.model.App;
import ru.practicum.stats_service.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final AppService appService;
    private final StatsService statsService;

    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        App appId = appService.getApp(endpointHit.getApp().getApp());
        if (appId == null) {
            endpointHit.setApp(appService.create(endpointHit.getApp()));
        } else {
            endpointHit.setApp(appId);
        }
        return statsService.addHit(endpointHit);
    }

    @Override
    public List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return statsService.get(start, end, uris, unique);
    }
}
