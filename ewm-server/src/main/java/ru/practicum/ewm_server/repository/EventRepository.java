package ru.practicum.ewm_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.enums.EventStateEnum;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {
    @Query(value = "select e from Event as e where e.initiator.id = ?1")
    Page<Event> getEvents(int userId, Pageable pageable);

    @Query(value = "select e from Event as e where e.initiator.id = ?1 and e.id = ?2")
    Event getEventByIdUserWitchId(int userId, int eventId);

    Event findByIdAndStateEquals(int eventId, EventStateEnum eventStateEnum);

    Set<Event> findAllByIdIn(Set<Integer> ids);

    @Query(value = "select e from Event as e where e.id in (?1)")
    Set<Event> getEventByIdForListCompilation(Set<Integer> ids);

    List<Integer> findAllByCategoryId(int catId);
}
