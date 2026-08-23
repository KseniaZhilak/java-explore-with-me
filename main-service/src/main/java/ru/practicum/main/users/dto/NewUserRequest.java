package ru.practicum.main.users.dto;

import jakarta.validation.constraints.Email;
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
public class NewUserRequest {

    @NotBlank(message = "Электронная почта пользователя не может быть пустой!")
    @Size(min = 6, max = 254)
    @Email
    private String email;

    @NotBlank(message = "Имя пользователя не может быть пустым!")
    @Size(min = 2, max = 250)
    private String name;

}
