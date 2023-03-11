package ru.practicum.ewm_server.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {
    @NotBlank
    float lat;
    @NotBlank
    float lon;
}
