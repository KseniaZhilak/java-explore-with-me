package ru.practicum.main.events;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.events.dto.*;
import ru.practicum.main.events.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.main.events.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.main.events.states.State;
import ru.practicum.main.requests.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
public class EventsController {

    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(
            @RequestBody @Valid NewEventDto eventDto,
            @PathVariable Long userId) {
        return eventsService.createEvent(eventDto, userId);
    }

    @GetMapping("/users/{userId}/events")
    public Collection<EventShortDto> getEventsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventsService.getEventsByUser(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventById(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventsService.getEventById(userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getRequestsInfoByUser(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventsService.getRequestsInfoByUser(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventStatus(
            @RequestBody @Valid EventRequestStatusUpdateRequest updateDto,
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventsService.updateEventStatus(updateDto, userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEvent(
            @RequestBody @Valid UpdateEventUserRequest eventDto,
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventsService.updateEvent(userId, eventId, eventDto);
    }

    @GetMapping("/admin/events")
    public Collection<EventFullDto> getEventsAdmin(
            @RequestParam(defaultValue = "") List<Long> users,
            @RequestParam(defaultValue = "") List<State> states,
            @RequestParam(defaultValue = "") List<Long> categories,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventsService.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("admin/events/{eventId}")
    public EventFullDto updateEventAdmin(
            @RequestBody @Valid UpdateEventAdminRequest eventAdminDto,
            @PathVariable Long eventId
    ) {
        return eventsService.updateEventAdmin(eventId, eventAdminDto);
    }

}
