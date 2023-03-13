package ru.practicum.ewm_server.web.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.EventFullDto;
import ru.practicum.ewm_server.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_server.enums.EventStateEnum;
import ru.practicum.ewm_server.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Validated
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
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Received a request from the administrator to search for events");
        return eventService.getEventsAdmin(usersId, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByIdAdmin(@RequestBody UpdateEventAdminRequest adminRequest,
                                             @PathVariable int eventId) {
        log.info("A request was received from the administrator to edit the event data under the id:{}", eventId);
        eventService.getById(eventId);
        return eventService.updateEventByIdAdmin(adminRequest, eventId);
    }
}
