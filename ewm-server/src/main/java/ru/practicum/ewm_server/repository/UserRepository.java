package ru.practicum.ewm_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_server.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findByIdIn(List<Integer> idS, Pageable pageable);
}
