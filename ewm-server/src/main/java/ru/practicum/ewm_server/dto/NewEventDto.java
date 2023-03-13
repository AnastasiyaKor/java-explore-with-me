package ru.practicum.ewm_server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @Size(min = 20, max = 2000, message = "Длина аннотации должна быть не менее 20 и не более 2000 символов")
    @NotNull
    private String annotation;
    @NotNull(message = "Необходимо указать категорию")
    private Integer category;
    @NotNull
    @Size(min = 20, max = 7000, message = "Длина описания должна быть не менее 20 и не более 7000 символов")
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @Valid
    private LocationDto location;
    private Boolean paid = false;
    @PositiveOrZero
    private int participantLimit;
    private boolean requestModeration = true;
    @NotNull
    @Size(min = 3, max = 120, message = "Длина заголовка должна быть не менее 3 и не более 120 символов")
    private String title;
}
