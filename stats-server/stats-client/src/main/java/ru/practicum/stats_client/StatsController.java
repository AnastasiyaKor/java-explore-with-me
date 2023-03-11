package ru.practicum.stats_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats_dto.EndpointHitDto;
import ru.practicum.stats_dto.ViewStats;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    public EndpointHitDto create(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Сохранение информации о том, что к эндпоинту был запрос");
        return statsClient.create(endpointHitDto);
    }

    @GetMapping("/stats?start=start&end=end&uris=uris&unique=unique")
    public List<ViewStats> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                               @RequestParam(defaultValue = "") List<String> uris,
                               @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Получен запрос на получение статистики по посещениям");
        return statsClient.get(start, end, uris, unique);
    }
}
