package ru.practicum.ewm_server.service.impl;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_server.dto.*;
import ru.practicum.ewm_server.entity.*;
import ru.practicum.ewm_server.enums.*;
import ru.practicum.ewm_server.exceptions.ConflictException;
import ru.practicum.ewm_server.exceptions.NotFoundException;
import ru.practicum.ewm_server.mapper.CommentMapper;
import ru.practicum.ewm_server.mapper.EventMapper;
import ru.practicum.ewm_server.mapper.RequestMapper;
import ru.practicum.ewm_server.repository.CommentRepository;
import ru.practicum.ewm_server.repository.EventRepository;
import ru.practicum.ewm_server.repository.RequestRepository;
import ru.practicum.ewm_server.service.CategoryService;
import ru.practicum.ewm_server.service.EventService;
import ru.practicum.ewm_server.service.RequestService;
import ru.practicum.ewm_server.service.statistic.StatisticService;
import ru.practicum.stats_dto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryService categoryService;
    private final RequestService requestService;
    private final StatisticService statisticService;

    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CommentRepository commentRepository;

    @Override
    public List<EventShortDto> getEventsUserId(int userId, int from, int size, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> eventList = eventRepository.getEvents(userId, pageable).getContent();
        if (eventList.isEmpty()) {
            return Collections.emptyList();
        }
        return addConfirmedRequest(eventList, request);
    }

    @Override
    @Transactional
    public EventFullDto create(Event event) {
        Category category = categoryService.getById(event.getCategory().getId());
        LocalDateTime minTime = LocalDateTime.now().minusHours(2);
        if (event.getEventDate().isAfter(minTime)) {
            event.getCategory().setName(category.getName());
            eventRepository.save(event);
            int confirmedRequest = requestService.getConfirmedRequest(event.getId());
            EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
            eventFullDto.setConfirmedRequests(confirmedRequest);
            return eventFullDto;
        } else {
            throw new ConflictException("Дата и время намеченного события не может быть раньше, " +
                    "чем через два часа от текущего момента");
        }
    }

    @Override
    public EventFullDto getEventByIdUserWithId(int userId, int eventId) {
        int confirmedRequest = requestService.getConfirmedRequest(eventId);
        Event event = eventRepository.getEventByIdUserWitchId(userId, eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(confirmedRequest);
        return eventFullDto;
    }

    @Override
    public Event getById(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие не найдено"));
    }

    @Transactional
    @Override
    public EventFullDto updateUserRequest(UpdateEventUserRequest userRequest, int userId, int eventId) {
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
        Event update = eventRepository.save(newEvent);
        int confirmedRequest = requestService.getConfirmedRequest(newEvent.getId());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(update);
        eventFullDto.setConfirmedRequests(confirmedRequest);
        return eventFullDto;
    }

    @Override
    public List<Request> getRequestsParticipationEventById(int userId, int eventId) {
        Event event = getById(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Событие недоступно");
        }
        return requestRepository.getRequests(eventId, userId);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequestEventById(
            EventRequestStatusUpdateRequest updateRequest, int userId, int eventId) {
        List<Request> confirmedList = new ArrayList<>();
        List<Request> rejectedList = new ArrayList<>();
        Event event = getById(eventId);
        int confirmedRequest = requestService.getConfirmedRequest(eventId);
        int count = confirmedRequest;
        if (confirmedRequest == event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит по заявкам на данное событие");
        }
        if (event.getParticipantLimit() != 0 || !event.getRequestModeration()) {
            List<Request> requests = requestRepository.getRequestByIdEventId(
                    updateRequest.getRequestIds(), RequestStatusEnum.PENDING);
            for (Request r : requests) {
                if (updateRequest.getStatus().equals(RequestStatusEnum.CONFIRMED)) {
                    if (confirmedRequest < event.getParticipantLimit() && count < event.getParticipantLimit()) {
                        r.setStatus(RequestStatusEnum.CONFIRMED);
                        confirmedList.add(r);
                        count++;
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
    public List<EventFullDto> getEventsAdmin(List<Integer> usersId, List<EventStateEnum> states,
                                             List<Integer> categories, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        QEvent event = QEvent.event;
        BooleanBuilder builder = new BooleanBuilder();
        if (usersId != null && !usersId.isEmpty()) {
            builder.and(event.initiator.id.in(usersId));
        }
        if (states != null && !states.isEmpty()) {
            builder.and(event.state.in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            builder.and(event.category.id.in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            builder.and(event.eventDate.after(LocalDateTime.now()));
        }
        if (rangeStart != null) {
            builder.and(event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            builder.and(event.eventDate.before(rangeEnd));
        }
        List<Event> eventList = eventRepository.findAll(builder, pageable).getContent();
        if (eventList.isEmpty()) {
            return Collections.emptyList();
        }
        return addConfirmedRequestFullDto(eventList);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByIdAdmin(UpdateEventAdminRequest adminRequest, int eventId) {
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
        Event event = eventRepository.save(newEventAdmin);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        int confirmedRequest = requestService.getConfirmedRequest(event.getId());
        eventFullDto.setConfirmedRequests(confirmedRequest);
        return eventFullDto;
    }

    @Override
    @Transactional
    public List<EventShortDto> getEventsPublic(String text, List<Integer> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, EventSortEnum sortEnum, int from, int size,
                                               HttpServletRequest request) {
        Pageable pageable = PageRequest.of(from / size, size);
        QEvent event = QEvent.event;
        BooleanBuilder builder = new BooleanBuilder();
        if (text != null && text.isBlank()) {
            builder.and(event.annotation.containsIgnoreCase(text).or(event.description.containsIgnoreCase(text)));
        }
        if (categories != null && !categories.isEmpty()) {
            builder.and(event.category.id.in(categories));
        }
        if (paid != null) {
            builder.and(event.paid.eq(paid));
        }
        if (rangeStart != null && rangeEnd != null) {
            builder.and(event.eventDate.after(LocalDateTime.now()));
        }
        if (rangeStart != null) {
            builder.and(event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            builder.and(event.eventDate.before(rangeEnd));
        }
        List<Event> events = eventRepository.findAll(builder, pageable).getContent();
        if (events.isEmpty()) {
            return Collections.emptyList();
        }
        List<EventShortDto> resultEventList;
        List<EventShortDto> eventShortDtoList = addConfirmedRequest(events, request);
        resultEventList = getEventListSorted(eventShortDtoList, events, onlyAvailable, sortEnum);
        return resultEventList;
    }

    @Override
    public EventFullDto getEventByIdPublic(int id, HttpServletRequest request) {
        String start = "2000-01-01 00:00:00";
        LocalDateTime end = LocalDateTime.now();
        Event event = eventRepository.findByIdAndStateEquals(id, EventStateEnum.PUBLISHED);
        int confirmedRequest = requestService.getConfirmedRequest(event.getId());
        List<ViewStats> viewStatsList = statisticService.getViews(LocalDateTime.parse(start, FORMATTER), end,
                List.of(request.getRequestURI() + "/" + event.getId()), true);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(confirmedRequest);
        if (!viewStatsList.isEmpty()) {
            eventFullDto.setViews((int) viewStatsList.get(0).getHits());
        } else {
            eventFullDto.setViews(0);
        }
        return eventFullDto;
    }

    public Set<Event> getEventForAdmin(Set<Integer> ids) {
        return eventRepository.findAllByIdIn(ids);
    }

    private List<EventShortDto> addConfirmedRequest(List<Event> resultEventList, HttpServletRequest request) {
        String start = "2000-01-01 00:00:00";
        LocalDateTime currentTime = LocalDateTime.now();
        String endTime = currentTime.format(FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endTime, FORMATTER);
        List<String> uris = List.of();
        Map<Integer, String> listUrisAndEventId = new HashMap<>();
        for (Event e : resultEventList) {
            uris = List.of("/events/" + e.getId());
            listUrisAndEventId.put(e.getId(), request.getRequestURI() + "/" + e.getId());
        }
        List<ViewStats> viewStatsList = statisticService.getViews(LocalDateTime.parse(start, FORMATTER),
                end, uris, true);
        Map<Integer, Long> mapEventsIdAndCountViews = new HashMap<>();
        for (Integer i : listUrisAndEventId.keySet()) {
            for (ViewStats vv : viewStatsList) {
                if (vv.getUri().equals(listUrisAndEventId.get(i))) {
                    mapEventsIdAndCountViews.put(i, vv.getHits());
                }
            }
        }
        List<EventShortDto> eventShortDtos = EventMapper.toListEventsShortDto(resultEventList);
        Map<Integer, Integer> getConfirmedRequests = requestService.getConfirmedRequest(resultEventList);
        for (EventShortDto e : eventShortDtos) {
            if (getConfirmedRequests.containsKey(e.getId())) {
                e.setConfirmedRequests(getConfirmedRequests.get(e.getId()));
            }
            if (mapEventsIdAndCountViews.containsKey(e.getId())) {
                e.setViews(mapEventsIdAndCountViews.get(e.getId()).intValue());
            }
        }
        return eventShortDtos;
    }

    private List<EventFullDto> addConfirmedRequestFullDto(List<Event> eventList) {
        List<EventFullDto> eventFullDtos = EventMapper.toListEventFullDto(eventList);
        Map<Integer, Integer> getConfirmedRequests = requestService.getConfirmedRequest(eventList);
        for (EventFullDto e : eventFullDtos) {
            if (getConfirmedRequests.containsKey(e.getId())) {
                e.setConfirmedRequests(getConfirmedRequests.get(e.getId()));
            }
        }
        return eventFullDtos;
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
        if (adminRequest.getAnnotation() != null && !adminRequest.getAnnotation().isBlank()) {
            newEvent.setAnnotation(adminRequest.getAnnotation());
        }
        if (adminRequest.getCategory() != null) {
            newEvent.setCategory(Category.builder()
                    .id(adminRequest.getCategory()).build());
        }
        if (adminRequest.getDescription() != null && !adminRequest.getDescription().isBlank()) {
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
        if (adminRequest.getTitle() != null && !adminRequest.getTitle().isBlank()) {
            newEvent.setTitle(adminRequest.getTitle());
        }
    }

    private void updateTheEventFieldUserRequest(UpdateEventUserRequest userRequest, Event newEvent) {
        if (userRequest.getAnnotation() != null && !userRequest.getAnnotation().isBlank()) {
            newEvent.setAnnotation(userRequest.getAnnotation());
        }
        if (userRequest.getCategory() != null) {
            newEvent.setCategory(Category.builder()
                    .id(userRequest.getCategory()).build());
        }
        if (userRequest.getDescription() != null && !userRequest.getDescription().isBlank()) {
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
        if (userRequest.getTitle() != null && !userRequest.getTitle().isBlank()) {
            newEvent.setTitle(userRequest.getTitle());
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

    private List<EventShortDto> getEventListSorted(List<EventShortDto> eventsDate, List<Event> events,
                                                   Boolean onlyAvailable, EventSortEnum sortEnum) {
        if (onlyAvailable.equals(true)) {
            if (sortEnum.equals(EventSortEnum.EVENT_DATE)) {
                return eventsDate.stream()
                        .filter(eventShortDto -> eventShortDto.getConfirmedRequests() <
                                events.get(eventShortDto.getId()).getParticipantLimit())
                        .sorted(Comparator.comparing(EventShortDto::getEventDate))
                        .collect(Collectors.toList());
            } else {
                return eventsDate.stream()
                        .filter(eventShortDto -> eventShortDto.getConfirmedRequests() <
                                events.get(eventShortDto.getId()).getParticipantLimit())
                        .sorted(Comparator.comparing(EventShortDto::getViews).reversed())
                        .collect(Collectors.toList());
            }
        } else {
            if (sortEnum.equals(EventSortEnum.EVENT_DATE)) {
                return eventsDate.stream()
                        .sorted(Comparator.comparing(EventShortDto::getEventDate))
                        .collect(Collectors.toList());
            } else {
                return eventsDate.stream()
                        .sorted(Comparator.comparing(EventShortDto::getViews).reversed())
                        .collect(Collectors.toList());
            }
        }
    }

    private EventCommentFullDto result(int eventId, EventFullDto eventFullDto) {
        List<Comment> comments = commentRepository.getCommentByEventId(eventId);
        if (!comments.isEmpty()) {
            List<CommentShortDto> commentShortDtos = CommentMapper.toListCommentShortDto(comments);
            return new EventCommentFullDto(eventFullDto, commentShortDtos);
        } else {
            return new EventCommentFullDto(eventFullDto, Collections.emptyList());
        }
    }
}
