package ru.practicum.main.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private Set<Long> events = new HashSet<>();

    @Builder.Default
    private Boolean pinned = false;

    @NotBlank(message = "Поле 'Заголовок подборки' не может быть пустым")
    @Size(min = 1, max = 50,
            message = "Длина поля должна быть от 1 до 50 символов")
    private String title;

}
