package ru.practicum.ewm_server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm_server.enums.EventStateActionEnum;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "Длина аннотации должна быть не менее 20 и не более 2000 символов")
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000, message = "Длина описания должна быть не менее 20 и не более 7000 символов")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateActionEnum stateAction;
    private String title;
}
