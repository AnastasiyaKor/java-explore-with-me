package ru.practicum.stats_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.stats_dto.EndpointHitDto;
import ru.practicum.stats_dto.ViewStats;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    public Mono<EndpointHitDto> create(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Сохранение информации о том, что к эндпоинту был запрос");
        return statsClient.create(endpointHitDto);
    }

    @GetMapping("/stats?start=start&end=end&uris=uris&unique=unique")
    public Mono<ViewStats> get(@RequestParam String start,
                               @RequestParam String end,
                               @RequestParam String[] uris,
                               @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Получен запрос на получение статистики по посещениям");
        return statsClient.get(start, end, uris, unique);
    }
}
