package ru.practicum.ewm_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_server.dto.NewCompilationDto;
import ru.practicum.ewm_server.entity.Compilation;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.exceptions.NotFoundException;
import ru.practicum.ewm_server.repository.CompilationRepository;
import ru.practicum.ewm_server.repository.EventRepository;
import ru.practicum.ewm_server.service.CompilationService;
import ru.practicum.ewm_server.service.EventService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;

    @Override
    public List<Compilation> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilationList;
        if (pinned == null) {
            compilationList = compilationRepository.getCompilationNotParam(pageable).getContent();
        } else {
            compilationList = compilationRepository.getCompilations(pinned, pageable).getContent();
        }
        if (!compilationList.isEmpty()) {
            return compilationList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Compilation getCompilationId(int compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка не найдена"));
    }

    @Override
    @Transactional
    public Compilation create(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents().isPresent()) {
            List<Event> events = eventService.getEventForAdmin(newCompilationDto.getEvents().get());
            return compilationRepository.save(new Compilation(events, newCompilationDto.getPinned(),
                    newCompilationDto.getTitle()));
        } else {
            return compilationRepository.save(new Compilation(Collections.emptyList(), newCompilationDto.getPinned(),
                    newCompilationDto.getTitle()));
        }
    }

    @Override
    @Transactional
    public void deleteId(int compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public Compilation update(NewCompilationDto newCompilationDto, int compId) {
        Compilation compilation = getCompilationId(compId);
        List<Event> events;
        if (newCompilationDto.getEvents().isPresent()) {
            events = eventRepository.getEventByIdForListCompilation(newCompilationDto.getEvents().get());
            compilation.setEvents(events);
        } else {
            compilation.setEvents(Collections.emptyList());
        }
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }
}
