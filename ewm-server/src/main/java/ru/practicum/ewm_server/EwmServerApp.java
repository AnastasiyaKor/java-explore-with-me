package ru.practicum.ewm_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.stats_client", "ru.practicum.ewm_server"})
public class EwmServerApp {
    public static void main(String[] args) {
        SpringApplication.run(EwmServerApp.class, args);
    }
}
