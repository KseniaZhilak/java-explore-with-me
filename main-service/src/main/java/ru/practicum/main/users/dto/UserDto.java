package ru.practicum.main.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "Электронная почта пользователя не может быть пустой!")
    private String email;

    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым!")
    private String name;
}
