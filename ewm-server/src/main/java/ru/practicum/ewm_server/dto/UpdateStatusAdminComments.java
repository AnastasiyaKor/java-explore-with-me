package ru.practicum.ewm_server.dto;

import lombok.*;
import ru.practicum.ewm_server.enums.CommentStateAdmin;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusAdminComments {
    private String commentAdmin;
    @NotNull
    private CommentStateAdmin stateAdmin;
}
