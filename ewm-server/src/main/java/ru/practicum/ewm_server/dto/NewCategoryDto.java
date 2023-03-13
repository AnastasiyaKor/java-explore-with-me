package ru.practicum.ewm_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank
    @Size(min = 3, max = 120, message = "Длина категории должна быть не менее 3 и не более 120 символов")
    private String name;
}
