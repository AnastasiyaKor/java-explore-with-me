package ru.practicum.ewm_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_server.entity.Request;
import ru.practicum.ewm_server.enums.RequestStatusEnum;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query(value = "select r from Request as r where r.event.id = ?1")
    List<Request> getRequests(int eventId);

    @Query(value = "select r from Request as r where r.id in (?1) and r.status = ?2")
    List<Request> getRequestByIdEventId(List<Integer> requestIds, RequestStatusEnum requestStatusEnum);

    List<Request> findByRequesterId(int userId);
}
