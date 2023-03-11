package ru.practicum.ewm_server.service;

import ru.practicum.ewm_server.entity.Request;

import java.util.List;

public interface RequestService {
    List<Request> getParticipationRequestUserId(int userId);

    Request createParticipationRequest(int userId, int eventId);

    Request cancelParticipationRequestId(int userId, int requestId);

    Request getById(int requestId);
}
