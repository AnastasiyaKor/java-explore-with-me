package ru.practicum.ewm_server.web.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.EventFullDto;
import ru.practicum.ewm_server.dto.EventShortDto;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.enums.EventSortEnum;
import ru.practicum.ewm_server.mapper.EventMapper;
import ru.practicum.ewm_server.service.EventService;
import ru.practicum.ewm_server.service.statistic.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;
    private final StatisticService statisticService;

    @GetMapping
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(defaultValue = "false") Boolean paid,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(defaultValue = "EVENT_DATE") EventSortEnum sort,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {
        statisticService.addHit(request);
        log.info("Received a public request to receive events with filtering");
        List<Event> events = eventService.getEventsPublic(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        return EventMapper.toListEventsShortDto(events);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventByIdPublic(@PathVariable int id, HttpServletRequest request) {
        statisticService.addHit(request);
        log.info("A public request was received for detailed information about the event under the id: {}", id);
        eventService.getById(id);
        Event event = eventService.getEventByIdPublic(id);
        return EventMapper.toEventFullDto(event);

    }
}
