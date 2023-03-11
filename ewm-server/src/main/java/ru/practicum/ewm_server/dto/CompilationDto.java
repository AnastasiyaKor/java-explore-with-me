package ru.practicum.ewm_server.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CompilationDto {
    private Integer id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;
}