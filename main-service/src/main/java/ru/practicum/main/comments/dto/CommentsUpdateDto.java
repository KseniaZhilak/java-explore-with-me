package ru.practicum.main.comments.dto;

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
public class CommentsUpdateDto {

    @NotBlank
    @Size(min = 5, max = 2000,
            message = "Длина поля должна быть от 5 до 2000 символов")
    private String text;

}
