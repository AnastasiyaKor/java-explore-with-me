package ru.practicum.ewm_server.service;

import ru.practicum.ewm_server.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm_server.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm_server.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_server.dto.UpdateEventUserRequest;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.entity.Request;
import ru.practicum.ewm_server.enums.EventSortEnum;
import ru.practicum.ewm_server.enums.EventStateEnum;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<Event> getEventsUserId(int userId, int from, int size);

    Event create(Event event);

    Event getEventByIdUserWithId(int userId, int eventId);

    Event getById(int eventId);

    Event updateUserRequest(UpdateEventUserRequest updateEventUserRequest, int userId, int eventId);

    List<Request> getRequestsParticipationEventById(int userId, int eventId);

    EventRequestStatusUpdateResult updateStatusRequestEventById(EventRequestStatusUpdateRequest updateRequest,
                                                                int userId, int eventId);

    List<Event> getEventsAdmin(List<Integer> usersId, List<EventStateEnum> states, List<Integer> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    Event updateEventByIdAdmin(UpdateEventAdminRequest adminRequest, int eventId);

    List<Event> getEventsPublic(String text, List<Integer> categories, Boolean paid,
                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                EventSortEnum sort, int from, int size);

    Event getEventByIdPublic(int id);

    List<Event> getEventForAdmin(List<Integer> ids);

    List<Event> getEventsByCategoryIdForAdmin(List<Integer> ids, int categoryId);
}
