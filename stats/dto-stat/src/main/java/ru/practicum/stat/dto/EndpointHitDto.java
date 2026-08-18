package ru.practicum.stat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {

    @NonNull
    @NotBlank(message = "Название сервиса не может быть пустым!")
    private String app;

    @NonNull
    @NotBlank(message = "Путь до сервиса не может быть пустым!")
    private String uri;

    @NonNull
    @NotBlank(message = "Адрес сервиса не может быть пустым!")
    private String ip;

    @NonNull
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    private Instant timestamp;

}
