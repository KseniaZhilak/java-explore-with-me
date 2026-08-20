package ru.practicum.main.categories.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @NonNull
    @NotBlank(message = "Название категории не может быть пустым!")
    String name;

}
