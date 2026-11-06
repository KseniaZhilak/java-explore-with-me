package ru.practicum.main.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @NotBlank(message = "Название категории не может быть пустым!")
    @Size(min = 1, max = 50, message = "Длина названия категории должна быть от 1 до 50 символов")
    private String name;

}
