package ru.practicum.stats_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats_service.model.App;
import ru.practicum.stats_service.repository.AppRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final AppRepository appRepository;

    @Override
    public App create(App app) {
        List<App> appList = appRepository.findAll();
        App saveApp;
        boolean any = appList.stream()
                .noneMatch(a -> a.getApp().equals(app.getApp()));
        if (appList.isEmpty() || any) {
            saveApp = appRepository.save(app);
        } else {
            saveApp = appList.stream()
                    .filter(a -> a.getApp().equals(app.getApp()))
                    .findAny().orElse(null);
        }
        return saveApp;
    }
}
