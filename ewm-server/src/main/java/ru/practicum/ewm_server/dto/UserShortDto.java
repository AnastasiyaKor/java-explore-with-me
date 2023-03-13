package ru.practicum.ewm_server.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserShortDto {
    private int id;
    private String name;
}
