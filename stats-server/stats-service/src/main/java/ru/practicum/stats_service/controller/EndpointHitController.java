package ru.practicum.stats_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats_dto.EndpointHitDto;
import ru.practicum.stats_dto.ViewStats;
import ru.practicum.stats_service.Mapper;
import ru.practicum.stats_service.model.EndpointHit;
import ru.practicum.stats_service.service.EndpointHitService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto create(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Сохранение информации о том, что к эндпоинту был запрос");
        EndpointHit endpointHit = Mapper.toEndpointHit(endpointHitDto);
        EndpointHit newEndpointHit = endpointHitService.create(endpointHit);
        return Mapper.toEndpointHitDto(newEndpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                               @RequestParam(defaultValue = "") List<String> uris,
                               @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Получен запрос на получение статистики по посещениям");
        return endpointHitService.get(start, end, uris, unique);
    }
}
