package ru.practicum.stats_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.stats_dto.EndpointHitDto;
import ru.practicum.stats_dto.ViewStats;


@Component
public class StatsClient {
    @Value("${stats.server.url}")
    String url;
    protected final WebClient webClient = WebClient.builder()
            .baseUrl(url)
            .build();

    public Mono<EndpointHitDto> create(EndpointHitDto endpointHitDto) {
        return webClient.post()
                .uri("/hit")
                .body(Mono.just(endpointHitDto), EndpointHitDto.class)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(EndpointHitDto.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                });
    }

    public Mono<ViewStats> get(String start, String end, String[] uris, boolean unique) {
        return webClient.get()
                .uri("/stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(ViewStats.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                });
    }
}
