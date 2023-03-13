package ru.practicum.ewm_server.dto;

import lombok.*;
import ru.practicum.ewm_server.marker.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCompilationDto {
    @NotBlank(groups = {Marker.Create.class})
    @Size(groups = {Marker.Create.class, Marker.Update.class}, min = 3, max = 120,
            message = "Длина подборки должна быть не менее 3 и не более 120 символов")
    private String title;
    @NotNull(groups = {Marker.Create.class})
    private Boolean pinned;
    private Optional<Set<Integer>> events;
}
