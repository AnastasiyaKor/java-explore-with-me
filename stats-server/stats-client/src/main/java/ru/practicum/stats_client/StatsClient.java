package ru.practicum.stats_client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.stats_dto.EndpointHitDto;
import ru.practicum.stats_dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StatsClient {
    private final WebClient webClient;

    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        return webClient
                .post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(endpointHitDto), EndpointHitDto.class)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        return Arrays.asList(Objects.requireNonNull(webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToMono(ViewStats[].class)
                .block()));
    }
}
