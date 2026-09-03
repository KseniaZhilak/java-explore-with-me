package ru.practicum.main.events.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotNull(message = "Широта не может быть пустой")
    private Float lat;

    @NotNull(message = "Долгота не может быть пустой")
    private Float lon;
}