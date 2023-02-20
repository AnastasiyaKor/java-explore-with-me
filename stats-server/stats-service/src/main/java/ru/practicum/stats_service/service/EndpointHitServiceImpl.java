package ru.practicum.stats_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats_dto.*;
import ru.practicum.stats_service.model.EndpointHit;
import ru.practicum.stats_service.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public EndpointHit create(EndpointHit endpointHit) {

        return endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> get(String start, String end, List<String> uris, boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<ViewStats> hits;
        if (uris != null) {
            if (!unique) {
                hits = endpointHitRepository.countHit(LocalDateTime.parse(start, formatter),
                        LocalDateTime.parse(end, formatter), uris);
            } else {
                hits = endpointHitRepository.countDistinctHit(LocalDateTime.parse(start, formatter),
                        LocalDateTime.parse(end, formatter), uris);
            }
        } else {
            return Collections.emptyList();
        }
        return hits;
    }
}
