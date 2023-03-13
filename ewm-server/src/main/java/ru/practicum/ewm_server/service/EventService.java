package ru.practicum.ewm_server.service;

import ru.practicum.ewm_server.dto.*;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.entity.Request;
import ru.practicum.ewm_server.enums.EventSortEnum;
import ru.practicum.ewm_server.enums.EventStateEnum;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    List<EventShortDto> getEventsUserId(int userId, int from, int size, HttpServletRequest request);

    EventFullDto create(Event event);

    EventFullDto getEventByIdUserWithId(int userId, int eventId);

    Event getById(int eventId);

    EventFullDto updateUserRequest(UpdateEventUserRequest updateEventUserRequest, int userId, int eventId);

    List<Request> getRequestsParticipationEventById(int userId, int eventId);

    EventRequestStatusUpdateResult updateStatusRequestEventById(EventRequestStatusUpdateRequest updateRequest,
                                                                int userId, int eventId);

    List<EventFullDto> getEventsAdmin(List<Integer> usersId, List<EventStateEnum> states, List<Integer> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventByIdAdmin(UpdateEventAdminRequest adminRequest, int eventId);

    List<EventShortDto> getEventsPublic(String text, List<Integer> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                        EventSortEnum sort, int from, int size, HttpServletRequest request);

    EventFullDto getEventByIdPublic(int id, HttpServletRequest request);

    Set<Event> getEventForAdmin(Set<Integer> ids);
}
