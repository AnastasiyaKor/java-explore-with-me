package ru.practicum.stats_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats_dto.ViewStats;
import ru.practicum.stats_service.model.App;
import ru.practicum.stats_service.model.EndpointHit;
import ru.practicum.stats_service.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;
    private final AppService appService;

    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        App newApp = appService.create(endpointHit.getApp());
        endpointHit.setApp(newApp);
        return endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> hits;
        if (!uris.isEmpty()) {
            if (!unique) {
                hits = endpointHitRepository.countHit(start, end, uris);
            } else {
                hits = endpointHitRepository.countDistinctHit(start, end, uris);
            }
        } else {
            if (!unique) {
                hits = endpointHitRepository.countHitWithoutUri(start, end);
            } else {
                hits = endpointHitRepository.countDistinctHitWithoutUri(start, end);
            }
        }
        return hits;
    }
}
