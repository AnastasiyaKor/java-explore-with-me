package ru.practicum.ewm_server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
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
    private int category;
    @Size(min = 20, max = 7000, message = "Длина описания должна быть не менее 20 и не более 7000 символов")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private boolean paid = false;
    private int participantLimit = 0;
    private boolean requestModeration = true;
    @Size(min = 3, max = 120, message = "Длина заголовка должна быть не менее 3 и не более 120 символов")
    private String title;
}
