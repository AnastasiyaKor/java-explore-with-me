package ru.practicum.ewm_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.entity.Request;
import ru.practicum.ewm_server.entity.User;
import ru.practicum.ewm_server.enums.EventStateEnum;
import ru.practicum.ewm_server.enums.RequestStatusEnum;
import ru.practicum.ewm_server.exceptions.ConflictException;
import ru.practicum.ewm_server.exceptions.NotFoundException;
import ru.practicum.ewm_server.repository.EventRepository;
import ru.practicum.ewm_server.repository.RequestRepository;
import ru.practicum.ewm_server.service.RequestService;
import ru.practicum.ewm_server.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserService userService;

    @Override
    public List<Request> getParticipationRequestUserId(int userId) {
        return requestRepository.findByRequesterId(userId);
    }

    @Override
    @Transactional
    public Request createParticipationRequest(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие не найдено"));
        User requester = userService.getById(userId);
        checkException(event, userId);
        LocalDateTime created = LocalDateTime.now();
        if (event.getRequestModeration().equals(false)) {
            return requestRepository.save(new Request(0, created, event, requester,
                    RequestStatusEnum.CONFIRMED));
        } else {
            return requestRepository.save(new Request(created, event, requester, RequestStatusEnum.PENDING));
        }
    }

    @Override
    @Transactional
    public Request cancelParticipationRequestId(int userId, int requestId) {
        Request request = getById(requestId);
        if (request.getRequester().getId() != userId) {
            throw new NotFoundException("Можно отменить только свой запрос");
        }
        request.setStatus(RequestStatusEnum.CANCELED);
        return request;
    }

    @Override
    public Request getById(int requestId) {
        return requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос на участие не найден"));
    }

    @Override
    public Integer getConfirmedRequest(int eventId) {
        return requestRepository.getConfirmedRequest(eventId).orElse(0);
    }

    @Override
    public Map<Integer, Integer> getConfirmedRequest(List<Event> events) {
        return requestRepository.getConfirmedRequestsListInteger(events);
    }

    private void checkException(Event event, int userId) {
        int confirmedRequest = getConfirmedRequest(event.getId());
        Request requests = requestRepository.getRequest(event.getId(), userId);
        if (requests != null) {
            throw new ConflictException("Запрос уже был отправлен");
        }
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Нельзя отправллять запрос на участие в своем событии");
        }
        if (event.getState().equals(EventStateEnum.PENDING) || event.getState().equals(EventStateEnum.CANCELED)) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }
        if (confirmedRequest == event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит запросов на участие");
        }
    }
}
