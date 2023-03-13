package ru.practicum.ewm_server.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String email;
    private int id;
    private String name;
}
