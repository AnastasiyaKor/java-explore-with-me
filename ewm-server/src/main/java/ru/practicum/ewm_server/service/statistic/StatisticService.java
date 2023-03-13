package ru.practicum.ewm_server.service.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.stats_client.StatsClient;
import ru.practicum.stats_dto.AppDto;
import ru.practicum.stats_dto.EndpointHitDto;
import ru.practicum.stats_dto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatsClient statsClient;
    @Value(value = "${app.name}")
    private String appName;

    public void addHit(HttpServletRequest request) {
        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setApp(new AppDto(appName));
        hitDto.setUri(request.getRequestURI());
        hitDto.setIp(request.getRemoteAddr());
        hitDto.setTimestamp(LocalDateTime.now());
        statsClient.create(hitDto);
    }

    public List<ViewStats> getViews(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return statsClient.get(start, end, uris, unique);

    }
}
