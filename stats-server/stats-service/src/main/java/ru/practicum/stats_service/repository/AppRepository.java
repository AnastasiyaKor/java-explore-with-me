package ru.practicum.stats_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stats_service.model.App;

@Repository
public interface AppRepository extends JpaRepository<App, Integer> {

}
