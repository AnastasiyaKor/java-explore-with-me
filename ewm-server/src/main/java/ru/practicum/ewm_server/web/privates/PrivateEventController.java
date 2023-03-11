package ru.practicum.ewm_server.web.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.*;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.entity.Request;
import ru.practicum.ewm_server.entity.User;
import ru.practicum.ewm_server.mapper.EventMapper;
import ru.practicum.ewm_server.mapper.RequestMapper;
import ru.practicum.ewm_server.service.EventService;
import ru.practicum.ewm_server.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;
    private final UserService userService;

    @GetMapping
    public List<EventShortDto> getEventsUserId(@PathVariable int userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        log.info("A request was received to receive events added by the user under the ID: {}", userId);
        userService.getById(userId);
        List<Event> events = eventService.getEventsUserId(userId, from, size);
        return EventMapper.toListEventsShortDto(events);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@RequestBody @Valid NewEventDto newEventDto, @PathVariable int userId) {
        log.info("A request was received from the user under the id: {} to add a new event", userId);
        User user = userService.getById(userId);
        Event event = eventService.create(EventMapper.toEvent(newEventDto, user));
        return EventMapper.toEventFullDto(event);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdUserWithId(@PathVariable int userId, @PathVariable int eventId) {
        log.info("A request was received from the user under the id: {} " +
                "to get full information about his event under the id: {}", userId, eventId);
        userService.getById(userId);
        eventService.getById(eventId);
        Event event = eventService.getEventByIdUserWithId(userId, eventId);
        return EventMapper.toEventFullDto(event);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateUserRequest(@RequestBody UpdateEventUserRequest updateEventUserRequest,
                                          @PathVariable int userId, @PathVariable int eventId) {
        log.info("A request was received from the user under the id: {} " +
                "to change the event under the id: {}", userId, eventId);
        userService.getById(userId);
        eventService.getById(eventId);
        Event event = eventService.updateUserRequest(
                updateEventUserRequest, userId, eventId);
        return EventMapper.toEventFullDto(event);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsParticipationEventById(@PathVariable int userId,
                                                                           @PathVariable int eventId) {
        log.info("A request was received from a user under the id: {}" +
                " to receive information about requests to participate in an event under the id: {}", userId, eventId);
        userService.getById(userId);
        eventService.getById(eventId);
        List<Request> requests = eventService.getRequestsParticipationEventById(userId, eventId);
        return RequestMapper.toListParticipationRequestDto(requests);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequestEventById(
            @RequestBody @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            EventRequestStatusUpdateRequest updateRequest,
            @PathVariable int userId, @PathVariable int eventId) {
        log.info("A request was received from the user under the id: {}" +
                        " to change the status of applications for participation in the event under the id: {}",
                userId, eventId);
        userService.getById(userId);
        eventService.getById(eventId);
        return eventService.updateStatusRequestEventById(updateRequest, userId, eventId);
    }
}
