package ru.practicum.main.requests;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.requests.dto.ParticipationRequestDto;

import java.util.Collection;

@RestController
public class RequestsController {

    private final RequestsService requestsService;

    public RequestsController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto createRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        return requestsService.createRequest(eventId, userId);
    }

    @GetMapping("/users/{userId}/requests")
    Collection<ParticipationRequestDto> getRequestsInfoByUser(@PathVariable Long userId) {
        return requestsService.getRequestsInfoByUser(userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto updateRequestStatus(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        return requestsService.updateRequestStatus(userId, requestId);
    }
}
