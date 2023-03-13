package ru.practicum.ewm_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_server.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
