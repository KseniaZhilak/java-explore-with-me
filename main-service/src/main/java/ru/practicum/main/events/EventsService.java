package ru.practicum.main.events;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.main.events.dto.*;
import ru.practicum.main.events.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.main.events.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.main.events.states.EventSort;
import ru.practicum.main.events.states.State;
import ru.practicum.main.requests.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventsService {

    EventFullDto createEvent(NewEventDto eventDto, Long userId);

    EventFullDto updateEvent(Long userId, Long id, UpdateEventUserRequest eventDto);

    EventFullDto getEventById(Long id, HttpServletRequest request);

    EventRequestStatusUpdateResult updateEventStatus(EventRequestStatusUpdateRequest updateDto, Long userId, Long eventId);

    EventFullDto updateEventAdmin(Long id, UpdateEventAdminRequest eventDto);

    Collection<EventShortDto> getEventsByUser(Long userId, int from, int size);

    EventFullDto getEventById(Long userId, Long id);

    Collection<ParticipationRequestDto> getRequestsInfoByUser(Long userId, Long eventId);

    Collection<EventFullDto> getEventsAdmin(List<Long> users, List<State> states, List<Long> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    Collection<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        Boolean onlyAvailable, EventSort sort, int from, int size,
                                        HttpServletRequest request);
}
