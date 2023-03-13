package ru.practicum.ewm_server.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
