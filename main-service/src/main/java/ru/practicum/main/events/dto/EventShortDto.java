package ru.practicum.main.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    private Long confirmedRequests;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Boolean paid;

    @NotBlank
    private String title;

    private Long views;

}
