package ru.practicum.main.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.main.events.dto.validation.AtLeastTwoHoursFromNow;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank(message = "Поле 'Краткое описание события' не может быть пустым")
    @Size(min = 20, max = 2000,
            message = "Длина поля должна быть от 20 до 2000 символов")
    private String annotation;

    @NotNull(message = "Поле 'Категория' не может быть пустым")
    private Long category;

    @NotBlank(message = "Поле 'Полное описание события' не может быть пустым")
    @Size(min = 20, max = 7000,
            message = "Длина поля должна быть от 20 до 7000 символов")
    private String description;

    @NotNull(message = "Поле 'Дата и время на которые намечено событие' не может быть пустым")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @AtLeastTwoHoursFromNow
    private LocalDateTime eventDate;

    @Valid
    @NotNull(message = "Поле 'Место проведения' не может быть пустым")
    private LocationDto location;

    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    private Integer participantLimit = 0;

    @Builder.Default
    private Boolean requestModeration = true;

    @NotBlank(message = "Поле 'Заголовок события' не может быть пустым")
    @Size(min = 3, max = 120,
            message = "Длина поля должна быть от 3 до 120 символов")
    private String title;
}