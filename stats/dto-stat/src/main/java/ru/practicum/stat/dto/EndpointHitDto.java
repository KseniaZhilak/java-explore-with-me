package ru.practicum.stat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}
