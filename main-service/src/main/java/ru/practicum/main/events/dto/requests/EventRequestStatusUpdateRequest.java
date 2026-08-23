package ru.practicum.main.events.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.events.states.Status;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private Status status;

}
