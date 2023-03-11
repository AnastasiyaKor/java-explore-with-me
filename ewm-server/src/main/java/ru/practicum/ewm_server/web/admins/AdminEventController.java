package ru.practicum.ewm_server.web.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.EventFullDto;
import ru.practicum.ewm_server.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.enums.EventStateEnum;
import ru.practicum.ewm_server.mapper.EventMapper;
import ru.practicum.ewm_server.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsAdmin(@RequestParam(required = false) List<Integer> usersId,
                                             @RequestParam(required = false) List<EventStateEnum> states,
                                             @RequestParam(required = false) List<Integer> categories,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                             LocalDateTime rangeStart,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                             LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("Received a request from the administrator to search for events");
        List<Event> events = eventService.getEventsAdmin(usersId, states, categories, rangeStart, rangeEnd, from, size);
        return EventMapper.toListEventFullDto(events);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByIdAdmin(@RequestBody UpdateEventAdminRequest adminRequest,
                                             @PathVariable int eventId) {
        log.info("A request was received from the administrator to edit the event data under the id:{}", eventId);
        eventService.getById(eventId);
        Event event = eventService.updateEventByIdAdmin(
                adminRequest, eventId);
        return EventMapper.toEventFullDto(event);
    }
}
