package ru.practicum.stats_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats_service.model.App;
import ru.practicum.stats_service.repository.AppRepository;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final AppRepository appRepository;

    @Override
    public App create(App app) {
        return appRepository.save(app);
    }

    @Override
    public App getApp(String app) {
        return appRepository.getApp(app);
    }
}
