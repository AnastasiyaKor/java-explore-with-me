package ru.practicum.ewm_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_server.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "select e.category.id from Category as c inner join Event as e on c.id=e.category.id " +
            "where e.category.id = ?1")
    List<Integer> getCategoryId(int catId);
}
