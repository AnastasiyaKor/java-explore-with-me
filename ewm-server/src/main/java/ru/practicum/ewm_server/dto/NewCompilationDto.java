package ru.practicum.ewm_server.dto;

import lombok.*;
import ru.practicum.ewm_server.marker.Marker;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCompilationDto {
    @NotNull(groups = {Marker.Create.class})
    private String title;
    @NotNull(groups = {Marker.Create.class})
    private Boolean pinned;
    private Optional<List<Integer>> events;
}
