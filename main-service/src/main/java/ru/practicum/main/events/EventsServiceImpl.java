package ru.practicum.main.events;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import ru.practicum.main.categories.repository.CategoriesEntity;
import ru.practicum.main.categories.repository.CategoriesRepository;
import ru.practicum.main.events.dto.*;
import ru.practicum.main.events.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.main.events.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.main.events.repository.EventsEntity;
import ru.practicum.main.events.repository.EventsRepository;
import ru.practicum.main.events.repository.mapper.EventsMapper;
import ru.practicum.main.events.states.EventSort;
import ru.practicum.main.events.states.State;
import ru.practicum.main.events.states.Status;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.ForbiddenException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.requests.StatusRequest;
import ru.practicum.main.requests.dto.ParticipationRequestDto;
import ru.practicum.main.requests.repository.RequestsEntity;
import ru.practicum.main.requests.repository.RequestsRepository;
import ru.practicum.main.requests.repository.mapper.RequestsMapper;
import ru.practicum.main.users.repository.UsersEntity;
import ru.practicum.main.users.repository.UsersRepository;
import ru.practicum.stat.client.StatClient;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.practicum.main.events.states.State.*;

@Slf4j
@Service
public class EventsServiceImpl implements EventsService {

    private static final String APP_NAME = "main-service";
    private static final String EVENT_URI = "/events/";
    private static final long NO_VIEWS = 0L;
    private static final int NO_LIMIT = 0;

    private final EventsRepository eventsRepository;
    private final EventsMapper eventsMapper;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;
    private final RequestsRepository requestsRepository;
    private final RequestsMapper requestsMapper;
    private final StatClient statClient;

    public EventsServiceImpl(EventsRepository eventsRepository, EventsMapper eventsMapper,
                             CategoriesRepository categoriesRepository, UsersRepository usersRepository,
                             RequestsRepository requestsRepository, RequestsMapper requestsMapper,
                             StatClient statClient) {
        this.eventsRepository = eventsRepository;
        this.eventsMapper = eventsMapper;
        this.categoriesRepository = categoriesRepository;
        this.usersRepository = usersRepository;
        this.requestsRepository = requestsRepository;
        this.requestsMapper = requestsMapper;
        this.statClient = statClient;
    }

    @Override
    @Transactional
    public EventFullDto createEvent(NewEventDto eventDto, Long userId) {
        CategoriesEntity category = categoriesRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        UsersEntity usersEntity = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        EventsEntity entity = eventsMapper.toEntity(eventDto);
        entity.setCategory(category);
        entity.setUser(usersEntity);
        entity.setState(PENDING);

        EventsEntity saved = eventsRepository.save(entity);
        EventFullDto eventFullDto = eventsMapper.toEventFullDto(saved);
        eventFullDto.setViews(NO_VIEWS);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long id, UpdateEventUserRequest eventDto) {
        EventsEntity entity = eventsRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (PUBLISHED.equals(entity.getState())) {
            throw new ForbiddenException("Event is already published");
        }

        if (eventDto.getCategory() != null) {
            entity.setCategory(categoriesRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found")));
        }

        if (eventDto.getStateAction() != null) {
            entity.setState(
                    switch (eventDto.getStateAction()) {
                        case SEND_TO_REVIEW -> State.PENDING;
                        case CANCEL_REVIEW -> State.CANCELED;
                    });
        }

        eventsMapper.updateEntity(eventDto, entity);
        EventsEntity saved = eventsRepository.save(entity);
        return toEventFullDtoWithViews(saved);
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        EventsEntity entity = eventsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Event not found")
        );
        if (!State.PUBLISHED.equals(entity.getState())) {
            throw new ForbiddenException("Event is not published");
        }
        saveHit(request);
        return toEventFullDtoWithViews(entity);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventStatus(
            EventRequestStatusUpdateRequest updateDto, Long userId, Long eventId) {
        EventsEntity entity = eventsRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();

        int limit = entity.getParticipantLimit() == null ? NO_LIMIT : entity.getParticipantLimit();
        if (limit == NO_LIMIT || Boolean.FALSE.equals(entity.getRequestModeration())) {
            return new EventRequestStatusUpdateResult(confirmed, rejected);
        }

        List<Long> requestIds = updateDto.getRequestIds() == null ? List.of() : updateDto.getRequestIds();
        List<RequestsEntity> requests = requestsRepository.findAllByIdInAndEventId(requestIds, eventId);
        if (requests.size() != Set.copyOf(requestIds).size()) {
            throw new NotFoundException("Request not found");
        }
        if (requests.stream().anyMatch(request -> !StatusRequest.PENDING.equals(request.getStatus()))) {
            throw new ConflictException("Only pending requests can be changed");
        }

        if (Status.REJECTED.equals(updateDto.getStatus())) {
            requests.forEach(request -> {
                request.setStatus(StatusRequest.REJECTED);
                rejected.add(requestsMapper.toDto(request));
            });
            requestsRepository.saveAll(requests);
            return new EventRequestStatusUpdateResult(confirmed, rejected);
        }

        if (entity.getConfirmedRequests() >= limit) {
            throw new ConflictException("Limit of participation requests reached");
        }

        long confirmedCount = entity.getConfirmedRequests();
        for (RequestsEntity request : requests) {
            if (confirmedCount < limit) {
                request.setStatus(StatusRequest.CONFIRMED);
                confirmedCount++;
                confirmed.add(requestsMapper.toDto(request));
            } else {
                request.setStatus(StatusRequest.REJECTED);
                rejected.add(requestsMapper.toDto(request));
            }
        }
        requestsRepository.saveAll(requests);

        entity.setConfirmedRequests(confirmedCount);
        eventsRepository.save(entity);

        if (confirmedCount >= limit) {
            Set<Long> handled = Set.copyOf(requestIds);
            List<RequestsEntity> stillPending = requestsRepository
                    .findAllByEventIdAndStatus(eventId, StatusRequest.PENDING).stream()
                    .filter(request -> !handled.contains(request.getId()))
                    .toList();
            stillPending.forEach(request -> {
                request.setStatus(StatusRequest.REJECTED);
                rejected.add(requestsMapper.toDto(request));
            });
            requestsRepository.saveAll(stillPending);
        }

        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(Long id, UpdateEventAdminRequest eventDto) {
        EventsEntity entity = eventsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (entity.getPublishedOn() != null) {
            if (!entity.getEventDate().isAfter(entity.getPublishedOn().plusHours(1))) {
                throw new ForbiddenException("Event is already published");
            }
        }

        if (eventDto.getStateAction() != null) {
            entity.setState(
                    switch (eventDto.getStateAction()) {
                        case PUBLISH_EVENT -> doUpdateState(entity, PUBLISHED);
                        case REJECT_EVENT -> doUpdateState(entity, CANCELED);
                    });
        }

        eventsMapper.updateAdminEntity(eventDto, entity);
        EventsEntity saved = eventsRepository.save(entity);
        return toEventFullDtoWithViews(saved);
    }

    @Override
    public Collection<EventShortDto> getEventsByUser(Long userId, int from, int size) {
        UsersEntity usersEntity = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Collection<EventsEntity> allByUserId = eventsRepository
                .findAllByUserId(usersEntity.getId(), from, size);

        Map<Long, Long> views = getViews(allByUserId);
        return allByUserId.stream()
                .map(entity -> {
                    EventShortDto dto = eventsMapper.toEventShortDto(entity);
                    dto.setViews(views.getOrDefault(entity.getId(), NO_VIEWS));
                    return dto;
                })
                .toList();
    }

    @Override
    public EventFullDto getEventById(Long userId, Long id) {
        EventsEntity entity = eventsRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        return toEventFullDtoWithViews(entity);
    }

    @Override
    public Collection<ParticipationRequestDto> getRequestsInfoByUser(Long userId, Long eventId) {
        eventsRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        return requestsRepository.findAllByEventId(eventId).stream()
                .map(requestsMapper::toDto)
                .toList();
    }

    @Override
    public Collection<EventFullDto> getEventsAdmin(List<Long> users, List<State> states, List<Long> categories,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   int from, int size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Range start must be before range end");
        }

        Collection<EventsEntity> eventsByFilters = eventsRepository.findEventsByFilters(
                users, !users.isEmpty(),
                states.stream().map(State::name).toList(), !states.isEmpty(),
                categories, !categories.isEmpty(),
                rangeStart, rangeEnd, from, size);

        Map<Long, Long> views = getViews(eventsByFilters);
        return eventsByFilters.stream()
                .map(entity -> {
                    EventFullDto dto = eventsMapper.toEventFullDto(entity);
                    dto.setViews(views.getOrDefault(entity.getId(), NO_VIEWS));
                    return dto;
                })
                .toList();
    }

    @Override
    public Collection<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, EventSort sort, int from, int size,
                                               HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Range start must be before range end");
        }

        boolean sortByViews = EventSort.VIEWS.equals(sort);
        Collection<EventsEntity> eventsByFilters = eventsRepository.findPublishedEventsByFilters(
                text,
                categories, !categories.isEmpty(),
                paid,
                rangeStart, rangeEnd,
                Boolean.TRUE.equals(onlyAvailable),
                sortByViews ? 0 : from,
                sortByViews ? Integer.MAX_VALUE : size);

        Map<Long, Long> views = getViews(eventsByFilters);
        Stream<EventShortDto> events = eventsByFilters.stream()
                .map(entity -> {
                    EventShortDto dto = eventsMapper.toEventShortDto(entity);
                    dto.setViews(views.getOrDefault(entity.getId(), NO_VIEWS));
                    return dto;
                });

        if (sortByViews) {
            events = events
                    .sorted(Comparator.comparing(EventShortDto::getViews).reversed())
                    .skip(from)
                    .limit(size);
        }
        saveHit(request);
        return events.toList();
    }

    private EventFullDto toEventFullDtoWithViews(EventsEntity entity) {
        EventFullDto dto = eventsMapper.toEventFullDto(entity);
        dto.setViews(getViews(List.of(entity)).getOrDefault(entity.getId(), NO_VIEWS));
        return dto;
    }

    private Map<Long, Long> getViews(Collection<EventsEntity> events) {
        List<EventsEntity> published = events.stream()
                .filter(entity -> entity.getPublishedOn() != null)
                .toList();
        if (published.isEmpty()) {
            return Map.of();
        }

        LocalDateTime start = published.stream()
                .map(EventsEntity::getPublishedOn)
                .min(LocalDateTime::compareTo)
                .orElseThrow();
        List<String> uris = published.stream()
                .map(entity -> EVENT_URI + entity.getId())
                .toList();

        try {
            return statClient.getHits(start, LocalDateTime.now(), true, uris).stream()
                    .collect(Collectors.toMap(
                            stats -> Long.valueOf(stats.getUri().substring(EVENT_URI.length())),
                            ViewStatsDto::getHits,
                            Long::sum));
        } catch (RestClientException e) {
            log.warn("Stats service is unavailable, views are set to {}: {}", NO_VIEWS, e.getMessage());
            return Map.of();
        }
    }

    private void saveHit(HttpServletRequest request) {
        try {
            statClient.createHit(APP_NAME, request.getRequestURI(), request.getRemoteAddr());
        } catch (RestClientException e) {
            log.warn("Stats service is unavailable, hit is not saved: {}", e.getMessage());
        }
    }

    private static State doUpdateState(EventsEntity entity, State newState) {
        if (!entity.getState().equals(PENDING)) {
            throw new ForbiddenException("Event is not in pending state");
        }
        entity.setState(newState);
        entity.setPublishedOn(LocalDateTime.now());
        return newState;
    }

}
