package ru.practicum.main.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.events.states.StateAdminAction;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000,
            message = "Длина поля должна быть от 20 до 2000 символов")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000,
            message = "Длина поля должна быть от 20 до 7000 символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAdminAction stateAction;

    @Size(min = 3, max = 120,
            message = "Длина поля должна быть от 3 до 120 символов")
    private String title;
}
