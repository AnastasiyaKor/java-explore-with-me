package ru.practicum.stats_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats_service.model.App;

@Repository
public interface AppRepository extends JpaRepository<App, Integer> {
    @Query(value = "select a from App as a where a.app =?1")
    App getApp(String app);

}
