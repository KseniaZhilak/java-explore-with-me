package ru.practicum.main.events.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotNull(message = "Широта не может быть пустой")
    private Float lat;

    @NotNull(message = "Долгота не может быть пустой")
    private Float lon;
}