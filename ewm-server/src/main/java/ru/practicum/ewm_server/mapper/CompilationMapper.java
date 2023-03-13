package ru.practicum.ewm_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_server.dto.CompilationDto;
import ru.practicum.ewm_server.dto.EventShortDto;
import ru.practicum.ewm_server.entity.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> shortDtoList = EventMapper.toSetEventsShortDto(compilation.getEvents());
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                shortDtoList
        );
    }

    public static List<CompilationDto> toCompilationDtoList(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
