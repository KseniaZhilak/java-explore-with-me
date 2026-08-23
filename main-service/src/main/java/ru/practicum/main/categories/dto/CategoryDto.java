package ru.practicum.main.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    Long id;

    @NotNull
    @NotBlank(message = "Название категории не может быть пустым!")
    String name;

}
