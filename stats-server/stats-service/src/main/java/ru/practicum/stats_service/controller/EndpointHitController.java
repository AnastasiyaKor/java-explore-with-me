package ru.practicum.stats_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats_dto.*;
import ru.practicum.stats_service.Mapper;
import ru.practicum.stats_service.model.EndpointHit;
import ru.practicum.stats_service.service.EndpointHitService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Valid
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto create(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Сохранение информации о том, что к эндпоинту был запрос");
        EndpointHit endpointHit = Mapper.toEndpointHit(endpointHitDto);
        return Mapper.toEndpointHitDto(endpointHitService.create(endpointHit));
    }

    @GetMapping("/stats")
    public List<ViewStats> get(@RequestParam(value = "start") String start,
                               @RequestParam(value = "end") String end,
                               @RequestParam(value = "uris") List<String> uris,
                               @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("Получен запрос на получение статистики по посещениям");
        return endpointHitService.get(start, end, uris, unique);
    }
}
