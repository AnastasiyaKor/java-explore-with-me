package ru.practicum.ewm_server.service;

import ru.practicum.ewm_server.dto.NewCompilationDto;
import ru.practicum.ewm_server.entity.Compilation;

import java.util.List;

public interface CompilationService {
    List<Compilation> getCompilations(Boolean pinned, int from, int size);

    Compilation getCompilationId(int compId);

    Compilation create(NewCompilationDto newCompilationDto);

    void deleteId(int compId);

    Compilation update(NewCompilationDto newCompilationDto, int compId);
}
