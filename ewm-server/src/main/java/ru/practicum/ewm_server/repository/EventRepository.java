package ru.practicum.ewm_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.enums.EventStateEnum;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query(value = "select e from Event as e where e.initiator.id = ?1")
    Page<Event> getEvents(int userId, Pageable pageable);

    @Query(value = "select e from Event as e where e.initiator.id = ?1 and e.id = ?2")
    Event getEventByIdUserWitchId(int userId, int eventId);

    @Query(value = "select e from Event as e where e.initiator.id in (?1) and e.state in (?2) " +
            "and e.category.id in (?3) and e.eventDate > ?4 and e.eventDate < ?5")
    Page<Event> getEventsForAdministrator(List<Integer> usersId, List<EventStateEnum> states,
                                          List<Integer> categories, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "select e from Event as e where  e.eventDate > ?1 order by e.eventDate DESC")
    Page<Event> getEventsForAdministratorNotParam(LocalDateTime currentTime, Pageable pageable);

    @Query(value = "select e from Event as e where (lower(e.annotation) like %?1% or " +
            "lower(e.description) like %?1%) " +
            "and e.category.id in (?2) and e.eventDate > ?3 and e.eventDate < ?4 and e.state = ?5")
    Page<Event> getEventListPublic(String text, List<Integer> categories, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, EventStateEnum eventStateEnum, Pageable pageable);

    @Query(value = "select e from Event as e where e.eventDate > ?1")
    Page<Event> getEventListPublicNotParam(LocalDateTime currentTime, Pageable pageable);

    @Query(value = "select e from Event as e where (lower(e.annotation) like %?1% or " +
            "lower(e.description) like %?1%) " +
            "and e.category.id in (?2)  and e.eventDate > ?3 and e.state = ?4")
    Page<Event> getEventListPublicNotDate(String text, List<Integer> categories, LocalDateTime currentDate,
                                          EventStateEnum eventStateEnum, Pageable pageable);

    Event findByIdAndStateEquals(int eventId, EventStateEnum eventStateEnum);

    List<Event> findAllByIdIn(List<Integer> ids);

    List<Event> findAllByIdInAndCategoryId(List<Integer> ids, int categoryId);

    @Query(value = "select e from Event as e where e.id in (?1)")
    List<Event> getEventByIdForListCompilation(List<Integer> ids);

}
