package ru.practicum.ewm_server.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EventCommentFullDto {
    private EventFullDto eventFullDto;
    private List<CommentShortDto> commentShortDto;
}
