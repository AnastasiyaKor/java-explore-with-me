package ru.practicum.ewm_server.web.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.CompilationDto;
import ru.practicum.ewm_server.dto.NewCompilationDto;
import ru.practicum.ewm_server.entity.Compilation;
import ru.practicum.ewm_server.mapper.CompilationMapper;
import ru.practicum.ewm_server.marker.Marker;
import ru.practicum.ewm_server.service.CompilationService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Validated({Marker.Create.class}) NewCompilationDto newCompilationDto) {
        log.info("Received a request from the administrator to add a new compilation");
        Compilation compilation = compilationService.create(newCompilationDto);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteId(@PathVariable int compId) {
        log.info("Received a request from the administrator to delete the compilation under the id: {}", compId);
        compilationService.deleteId(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@RequestBody @Validated(Marker.Update.class) NewCompilationDto newCompilationDto,
                                 @PathVariable int compId) {
        log.info("Received a request from the administrator to update the compilation under the id: {}", compId);
        Compilation compilation = compilationService.update(newCompilationDto, compId);
        return CompilationMapper.toCompilationDto(compilation);
    }
}
