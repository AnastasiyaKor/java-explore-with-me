package ru.practicum.ewm_server.web.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.*;
import ru.practicum.ewm_server.entity.Request;
import ru.practicum.ewm_server.entity.User;
import ru.practicum.ewm_server.mapper.EventMapper;
import ru.practicum.ewm_server.mapper.RequestMapper;
import ru.practicum.ewm_server.service.EventService;
import ru.practicum.ewm_server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size,
                                               HttpServletRequest request) {
        log.info("A request was received to receive events added by the user under the ID: {}", userId);
        userService.getById(userId);
        return eventService.getEventsUserId(userId, from, size, request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@RequestBody @Valid NewEventDto newEventDto, @PathVariable int userId) {
        log.info("A request was received from the user under the id: {} to add a new event", userId);
        User user = userService.getById(userId);
        return eventService.create(EventMapper.toEvent(newEventDto, user));
    }

    @GetMapping("/{eventId}")
    public EventCommentFullDto getEventByIdUserWithId(@PathVariable int userId, @PathVariable int eventId) {
        log.info("A request was received from the user under the id: {} " +
                "to get full information about his event under the id: {}", userId, eventId);
        userService.getById(userId);
        eventService.getById(eventId);
        return eventService.getEventByIdUserWithId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateUserRequest(@RequestBody @Valid UpdateEventUserRequest updateEventUserRequest,
                                          @PathVariable int userId, @PathVariable int eventId) {
        log.info("A request was received from the user under the id: {} " +
                "to change the event under the id: {}", userId, eventId);
        userService.getById(userId);
        eventService.getById(eventId);
        return eventService.updateUserRequest(updateEventUserRequest, userId, eventId);
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
