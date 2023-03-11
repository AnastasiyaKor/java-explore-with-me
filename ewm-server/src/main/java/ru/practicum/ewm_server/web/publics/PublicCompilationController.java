package ru.practicum.ewm_server.web.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.CompilationDto;
import ru.practicum.ewm_server.entity.Compilation;
import ru.practicum.ewm_server.mapper.CompilationMapper;
import ru.practicum.ewm_server.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("received a public request to receive collections of events");
        List<Compilation> compilations = compilationService.getCompilations(pinned, from, size);
        return CompilationMapper.toCompilationDtoList(compilations);
    }

    @GetMapping("{compId}")
    public CompilationDto getCompilationId(@PathVariable int compId) {
        log.info("a public request was received to receive a collection of events with the id= {}", compId);
        Compilation compilation = compilationService.getCompilationId(compId);
        return CompilationMapper.toCompilationDto(compilation);
    }
}
