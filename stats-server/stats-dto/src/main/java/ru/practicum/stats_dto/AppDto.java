package ru.practicum.stats_dto;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppDto {
    @NotBlank
    private String app;
}
