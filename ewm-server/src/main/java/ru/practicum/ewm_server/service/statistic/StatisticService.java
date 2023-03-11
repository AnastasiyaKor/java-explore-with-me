package ru.practicum.ewm_server.service.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats_client.StatsClient;
import ru.practicum.stats_dto.AppDto;
import ru.practicum.stats_dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatsClient statsClient;

    public void addHit(HttpServletRequest request) {
        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setApp(new AppDto("ewm-main-service"));
        hitDto.setUri(request.getRequestURI());
        hitDto.setIp(request.getRemoteAddr());
        hitDto.setTimestamp(LocalDateTime.now());
        statsClient.create(hitDto);
    }
}
