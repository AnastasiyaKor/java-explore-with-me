package ru.practicum.stats_service;

import lombok.experimental.UtilityClass;
import ru.practicum.stats_dto.AppDto;
import ru.practicum.stats_dto.EndpointHitDto;
import ru.practicum.stats_dto.ViewStats;
import ru.practicum.stats_service.model.App;
import ru.practicum.stats_service.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Mapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(App.builder()
                        .app(endpointHitDto.getApp().getApp())
                        .build())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), formatter))
                .build();
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto
                .builder()
                .app(AppDto.builder()
                        .app(endpointHit.getApp().getApp())
                        .build())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp().toString())
                .build();
    }

    public static ViewStats toViewStats(EndpointHit endpointHit) {
        return ViewStats.builder()
                .app(endpointHit.getApp().getApp())
                .uri(endpointHit.getUri())
                .build();
    }

    public static AppDto toAppDto(App app) {
        return AppDto.builder()
                .app(app.getApp())
                .build();
    }

    public static App toApp(AppDto appDto) {
        return App.builder()
                .app(appDto.getApp())
                .build();
    }
}
