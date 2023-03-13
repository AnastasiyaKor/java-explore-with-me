package ru.practicum.ewm_server.web.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.ParticipationRequestDto;
import ru.practicum.ewm_server.entity.Request;
import ru.practicum.ewm_server.mapper.RequestMapper;
import ru.practicum.ewm_server.service.RequestService;
import ru.practicum.ewm_server.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {
    private final RequestService requestService;
    private final UserService userService;

    @GetMapping
    public List<ParticipationRequestDto> getParticipationRequestUserId(@PathVariable int userId) {
        log.info("A request was received to receive information about " +
                "applications for participation in events from a user under the id: {}", userId);
        userService.getById(userId);
        List<Request> requests = requestService.getParticipationRequestUserId(userId);
        return RequestMapper.toListParticipationRequestDto(requests);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable int userId, @RequestParam int eventId) {
        log.info("A request was received to add a request to participate in an event id: {}" +
                " from a user under the id: {}", eventId, userId);
        return RequestMapper.toParticipationRequestDto(requestService.createParticipationRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequestId(@PathVariable int userId,
                                                                @PathVariable int requestId) {
        log.info("A request was received from the user under the id: " +
                "{} to cancel his participation in the event", userId);
        userService.getById(userId);
        requestService.getById(requestId);
        return RequestMapper.toParticipationRequestDto(requestService.cancelParticipationRequestId(userId, requestId));
    }
}
