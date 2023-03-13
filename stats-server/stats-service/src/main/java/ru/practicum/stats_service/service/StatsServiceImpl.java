package ru.practicum.stats_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats_dto.ViewStats;
import ru.practicum.stats_service.model.EndpointHit;
import ru.practicum.stats_service.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final EndpointHitRepository hitRepository;

    @Override
    @Transactional
    public EndpointHit addHit(EndpointHit endpointHit) {
        hitRepository.save(endpointHit);
        return endpointHit;
    }

    @Override
    public List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (!uris.isEmpty()) {
            if (!unique) {
                return hitRepository.countHit(start, end, uris);
            } else {
                return hitRepository.countDistinctHit(start, end, uris);
            }
        } else {
            if (!unique) {
                return hitRepository.countHitWithoutUri(start, end);
            } else {
                return hitRepository.countDistinctHitWithoutUri(start, end);
            }
        }
    }
}
