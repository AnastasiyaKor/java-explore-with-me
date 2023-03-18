package ru.practicum.ewm_server.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NewCommentDto {
    @NotBlank
    private String comment;
}
