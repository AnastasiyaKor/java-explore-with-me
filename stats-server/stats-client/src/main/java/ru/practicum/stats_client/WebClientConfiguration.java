package ru.practicum.stats_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan
public class WebClientConfiguration {
    @Bean
    public WebClient webClient(@Value(value = "http://localhost:9090") String serverUrl) {
        return WebClient.builder()
                .baseUrl(serverUrl)
                .build();
    }
}
