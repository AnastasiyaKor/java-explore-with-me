package ru.practicum.stats_dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStats {
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @NotBlank
    Long hits;
}
