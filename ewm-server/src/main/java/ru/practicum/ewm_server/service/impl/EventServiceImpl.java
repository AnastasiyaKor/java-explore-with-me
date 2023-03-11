package ru.practicum.ewm_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_server.dto.*;
import ru.practicum.ewm_server.entity.Category;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.entity.Location;
import ru.practicum.ewm_server.entity.Request;
import ru.practicum.ewm_server.enums.*;
import ru.practicum.ewm_server.exceptions.ConflictException;
import ru.practicum.ewm_server.exceptions.NotFoundException;
import ru.practicum.ewm_server.mapper.RequestMapper;
import ru.practicum.ewm_server.repository.EventRepository;
import ru.practicum.ewm_server.repository.RequestRepository;
import ru.practicum.ewm_server.service.CategoryService;
import ru.practicum.ewm_server.service.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryService categoryService;

    @Override
    public List<Event> getEventsUserId(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> eventList = eventRepository.getEvents(userId, pageable).getContent();
        if (!eventList.isEmpty()) {
            return eventList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public Event create(Event event) {
        Category category = categoryService.getById(event.getCategory().getId());
        LocalDateTime minTime = LocalDateTime.now().minusHours(2);
        if (event.getEventDate().isAfter(minTime)) {
            event.getCategory().setName(category.getName());
            return eventRepository.save(event);
        } else {
            throw new ConflictException("Дата и время намеченного события не может быть раньше, " +
                    "чем через два часа от текущего момента");
        }
    }

    @Override
    public Event getEventByIdUserWithId(int userId, int eventId) {
        return eventRepository.getEventByIdUserWitchId(userId, eventId);
    }

    @Override
    public Event getById(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие не найдено"));
    }

    @Transactional
    @Override
    public Event updateUserRequest(UpdateEventUserRequest userRequest, int userId, int eventId) {
        LocalDateTime minTime = LocalDateTime.now().minusHours(2);
        Event newEvent = getById(eventId);
        Category category = categoryService.getById(newEvent.getCategory().getId());
        checkException(newEvent.getInitiator().getId(), userId, newEvent.getState());
        updateTheEventFieldUserRequest(userRequest, newEvent);
        updateTheEventDateUser(userRequest, newEvent, minTime);
        if (userRequest.getCategory() != null) {
            category = categoryService.getById(userRequest.getCategory());
        }
        if (userRequest.getStateAction() != null) {
            if (userRequest.getStateAction().equals(EventStateActionEnum.SEND_TO_REVIEW)) {
                newEvent.setState(EventStateEnum.PENDING);
                newEvent.getCategory().setName(category.getName());
            }
            if (userRequest.getStateAction().equals(EventStateActionEnum.CANCEL_REVIEW)) {
                newEvent.setState(EventStateEnum.CANCELED);
            }
        }
        return eventRepository.save(newEvent);
    }

    @Override
    public List<Request> getRequestsParticipationEventById(int userId, int eventId) {
        Event event = getById(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Событие недоступно");
        }
        List<Request> requests = requestRepository.getRequests(eventId);
        if (!requests.isEmpty()) {
            return requests;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequestEventById(
            EventRequestStatusUpdateRequest updateRequest, int userId, int eventId) {
        List<Request> confirmedList = new ArrayList<>();
        List<Request> rejectedList = new ArrayList<>();
        Event event = getById(eventId);
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит по заявкам на данное событие");
        }
        if (event.getParticipantLimit() != 0 || !event.getRequestModeration()) {
            List<Request> requests = requestRepository.getRequestByIdEventId(
                    updateRequest.getRequestIds(), RequestStatusEnum.PENDING);
            for (Request r : requests) {
                if (updateRequest.getStatus().equals(RequestStatusEnum.CONFIRMED)) {
                    if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                        r.setStatus(RequestStatusEnum.CONFIRMED);
                        confirmedList.add(r);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else {
                        throw new ConflictException("Достигнут лимит заявок");
                    }
                } else {
                    if (r.getStatus().equals(RequestStatusEnum.CONFIRMED)) {
                        throw new ConflictException("Заявка уже принята");
                    }
                    r.setStatus(RequestStatusEnum.REJECTED);
                    rejectedList.add(r);
                }
            }
        }
        List<ParticipationRequestDto> confirmedRequests = RequestMapper
                .toListParticipationRequestDto(confirmedList);
        List<ParticipationRequestDto> rejectedRequests = RequestMapper
                .toListParticipationRequestDto(rejectedList);
        if (confirmedRequests.isEmpty()) {
            return new EventRequestStatusUpdateResult(null, rejectedRequests);
        }
        if (rejectedRequests.isEmpty()) {
            return new EventRequestStatusUpdateResult(confirmedRequests, null);
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<Event> getEventsAdmin(List<Integer> usersId, List<EventStateEnum> states,
                                      List<Integer> categories, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime currentTime = LocalDateTime.now();
        List<Event> eventList;
        if (usersId == null || states == null || categories == null || rangeStart == null || rangeEnd == null) {
            eventList = eventRepository.getEventsForAdministratorNotParam(currentTime, pageable).getContent();
        } else {
            eventList = eventRepository.getEventsForAdministrator(usersId, states, categories, rangeStart,
                    rangeEnd, pageable).getContent();
        }
        if (!eventList.isEmpty()) {
            return eventList;
        } else
            return Collections.emptyList();
    }

    @Override
    @Transactional
    public Event updateEventByIdAdmin(UpdateEventAdminRequest adminRequest, int eventId) {
        Event newEventAdmin = getById(eventId);
        Category category = categoryService.getById(newEventAdmin.getCategory().getId());
        LocalDateTime minTime = LocalDateTime.now().minusHours(1);
        LocalDateTime publishedDate = LocalDateTime.now();
        updateTheEventFieldAdminRequest(adminRequest, newEventAdmin);
        if (adminRequest.getCategory() != null) {
            category = categoryService.getById(adminRequest.getCategory());
        }
        updateTheEventDate(adminRequest, newEventAdmin, minTime);
        if (adminRequest.getStateAction() != null) {
            if (adminRequest.getStateAction().equals(EventStateActionAdminEnum.PUBLISH_EVENT)) {
                if (newEventAdmin.getState().equals(EventStateEnum.PENDING)) {
                    newEventAdmin.setState(EventStateEnum.PUBLISHED);
                    newEventAdmin.setPublishedOn(publishedDate);
                    newEventAdmin.getCategory().setName(category.getName());
                } else {
                    throw new ConflictException("событие можно публиковать, " +
                            "только если оно в состоянии ожидания публикации");
                }
            }
            if (adminRequest.getStateAction().equals(EventStateActionAdminEnum.REJECT_EVENT)) {
                if (newEventAdmin.getState().equals(EventStateEnum.PENDING)) {
                    newEventAdmin.setState(EventStateEnum.CANCELED);
                } else {
                    throw new ConflictException("Событие можно отклонить, " +
                            "только если оно еще не опубликовано");
                }
            }
        }
        return eventRepository.save(newEventAdmin);
    }


    @Override
    @Transactional
    public List<Event> getEventsPublic(String text, List<Integer> categories, Boolean paid,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                       EventSortEnum sortEnum, int from, int size) {
        List<Event> resultEventList;
        List<Event> eventsByDate = resultList(text, rangeStart, rangeEnd, categories, from, size);
        if (paid.equals(true)) {
            List<Event> eventPaid = getPaid(eventsByDate, true);
            addCountViewList(eventPaid);
            resultEventList = getEventListSorted(eventPaid, onlyAvailable, sortEnum);
        } else {
            List<Event> eventPaid = getPaid(eventsByDate, false);
            addCountViewList(eventPaid);
            resultEventList = getEventListSorted(eventPaid, onlyAvailable, sortEnum);
        }
        if (!resultEventList.isEmpty()) {
            return resultEventList;
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    @Transactional
    public Event getEventByIdPublic(int id) {
        Event event = eventRepository.findByIdAndStateEquals(id, EventStateEnum.PUBLISHED);
        event.setViews(event.getViews() + 1);
        return event;
    }

    public List<Event> getEventForAdmin(List<Integer> ids) {
        return eventRepository.findAllByIdIn(ids);
    }

    public List<Event> getEventsByCategoryIdForAdmin(List<Integer> ids, int categoryId) {
        return eventRepository.findAllByIdInAndCategoryId(ids, categoryId);
    }

    private void checkException(int initiatorId, int userId, EventStateEnum eventStateEnum) {
        if (initiatorId != userId) {
            throw new NotFoundException("Событие недоступно");
        }
        if (eventStateEnum.equals(EventStateEnum.PUBLISHED)) {
            throw new ConflictException("Событие уже опубликовано");
        }
    }

    private void updateTheEventFieldAdminRequest(UpdateEventAdminRequest adminRequest, Event newEvent) {
        if (adminRequest.getAnnotation() != null) {
            newEvent.setAnnotation(adminRequest.getAnnotation());
        }
        if (adminRequest.getCategory() != null) {
            newEvent.setCategory(Category.builder()
                    .id(adminRequest.getCategory()).build());
        }
        if (adminRequest.getDescription() != null) {
            newEvent.setDescription(adminRequest.getDescription());
        }
        if (adminRequest.getLocation() != null) {
            newEvent.setLocation(Location.builder()
                    .lat(adminRequest.getLocation().getLat()).lon(adminRequest.getLocation().getLon()).build());
        }
        if (adminRequest.getPaid() != null) {
            newEvent.setPaid(adminRequest.getPaid());
        }
        if (adminRequest.getParticipantLimit() != null) {
            newEvent.setParticipantLimit(adminRequest.getParticipantLimit());
        }
        if (adminRequest.getRequestModeration() != null) {
            newEvent.setRequestModeration(adminRequest.getRequestModeration());
        }
        if (adminRequest.getTitle() != null) {
            newEvent.setTitle(adminRequest.getTitle());
        }
    }

    private void updateTheEventFieldUserRequest(UpdateEventUserRequest userRequest, Event newEvent) {
        if (userRequest.getAnnotation() != null) {
            newEvent.setAnnotation(userRequest.getAnnotation());
        }
        if (userRequest.getCategory() != null) {
            newEvent.setCategory(Category.builder()
                    .id(userRequest.getCategory()).build());
        }
        if (userRequest.getDescription() != null) {
            newEvent.setDescription(userRequest.getDescription());
        }
        if (userRequest.getLocation() != null) {
            newEvent.setLocation(Location.builder()
                    .lat(userRequest.getLocation().getLat()).lon(userRequest.getLocation().getLon()).build());
        }
        if (userRequest.getPaid() != null) {
            newEvent.setPaid(userRequest.getPaid());
        }
        if (userRequest.getParticipantLimit() != null) {
            newEvent.setParticipantLimit(userRequest.getParticipantLimit());
        }
        if (userRequest.getRequestModeration() != null) {
            newEvent.setRequestModeration(userRequest.getRequestModeration());
        }
    }

    private void updateTheEventDate(UpdateEventAdminRequest adminRequest, Event updateEvent, LocalDateTime minTime) {
        if (adminRequest.getEventDate() != null) {
            if (adminRequest.getEventDate().isAfter(minTime)) {
                updateEvent.setEventDate(updateEvent.getEventDate());
            } else {
                throw new ConflictException("Дата и время намеченного события не может быть раньше, " +
                        "чем через два часа от текущего момента");
            }
        }
    }

    private void updateTheEventDateUser(UpdateEventUserRequest userRequest, Event updateEvent, LocalDateTime minTime) {
        if (userRequest.getEventDate() != null) {
            if (userRequest.getEventDate().isAfter(minTime)) {
                updateEvent.setEventDate(updateEvent.getEventDate());
            } else {
                throw new ConflictException("Дата и время намеченного события не может быть раньше, " +
                        "чем через два часа от текущего момента");
            }
        }
    }

    private List<Event> getPaid(List<Event> eventsDate, Boolean paid) {
        if (paid.equals(true)) {
            return eventsDate.stream()
                    .filter(event -> event.getPaid().equals(true))
                    .collect(Collectors.toList());
        } else {
            return eventsDate.stream()
                    .filter(event -> event.getPaid().equals(false))
                    .collect(Collectors.toList());
        }
    }

    private List<Event> getEventListSorted(List<Event> eventsDate, Boolean onlyAvailable, EventSortEnum sortEnum) {
        if (onlyAvailable.equals(true)) {
            if (sortEnum.equals(EventSortEnum.EVENT_DATE)) {
                return eventsDate.stream()
                        .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit())
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());
            } else {
                return eventsDate.stream()
                        .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit())
                        .sorted(Comparator.comparing(Event::getViews).reversed())
                        .collect(Collectors.toList());
            }
        } else {
            if (sortEnum.equals(EventSortEnum.EVENT_DATE)) {
                return eventsDate.stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());
            } else {
                return eventsDate.stream()
                        .sorted(Comparator.comparing(Event::getViews).reversed())
                        .collect(Collectors.toList());
            }
        }
    }

    private void addCountViewList(List<Event> events) {
        if (!events.isEmpty()) {
            for (Event e : events) {
                e.setViews(e.getViews() + 1);
                eventRepository.save(e);
            }
        }
    }

    private List<Event> resultList(String text, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   List<Integer> categories, int from, int size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentDate = LocalDateTime.now();
        String formatDateTime = currentDate.format(formatter);
        Pageable pageable = PageRequest.of(from / size, size);
        if (text == null || rangeStart == null || rangeEnd == null || categories == null) {
            return eventRepository.getEventListPublicNotParam(currentDate, pageable).getContent();
        }
        if (rangeStart != null && rangeEnd != null) {
            return eventRepository.getEventListPublic(text.toLowerCase(), categories, rangeStart, rangeEnd,
                    EventStateEnum.PUBLISHED, pageable).getContent();
        } else {
            return eventRepository.getEventListPublicNotDate(text.toLowerCase(), categories,
                    LocalDateTime.parse(formatDateTime, formatter), EventStateEnum.PUBLISHED, pageable).getContent();
        }
    }
}
