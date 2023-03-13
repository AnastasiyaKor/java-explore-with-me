package ru.practicum.ewm_server.service;

import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.entity.Request;

import java.util.List;
import java.util.Map;

public interface RequestService {
    List<Request> getParticipationRequestUserId(int userId);

    Request createParticipationRequest(int userId, int eventId);

    Request cancelParticipationRequestId(int userId, int requestId);

    Request getById(int requestId);

    Integer getConfirmedRequest(int eventId);

    Map<Integer, Integer> getConfirmedRequest(List<Event> events);

}
