package ru.practicum.main.requests;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.events.repository.EventsEntity;
import ru.practicum.main.events.repository.EventsRepository;
import ru.practicum.main.events.states.State;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.requests.dto.ParticipationRequestDto;
import ru.practicum.main.requests.repository.RequestsEntity;
import ru.practicum.main.requests.repository.RequestsRepository;
import ru.practicum.main.requests.repository.mapper.RequestsMapper;
import ru.practicum.main.users.repository.UsersEntity;
import ru.practicum.main.users.repository.UsersRepository;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
public class RequestsServiceImpl implements RequestsService {

    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;
    private final RequestsMapper requestsMapper;

    public RequestsServiceImpl(RequestsRepository requestsRepository, EventsRepository eventsRepository, UsersRepository usersRepository, RequestsMapper requestsMapper) {
        this.requestsRepository = requestsRepository;
        this.eventsRepository = eventsRepository;
        this.usersRepository = usersRepository;
        this.requestsMapper = requestsMapper;
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long eventId, Long userId) {
        UsersEntity usersEntity = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        EventsEntity entity = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (requestsRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new ConflictException("Request already exists");
        }
        if (usersEntity.getId().equals(entity.getUser().getId())) {
            throw new ConflictException("You cannot participate in your own event");
        }
        if (!State.PUBLISHED.equals(entity.getState())) {
            throw new ConflictException("Event is not published");
        }
        if (entity.getParticipantLimit() > 0 && entity.getConfirmedRequests() >= entity.getParticipantLimit()) {
            throw new ConflictException("Limit of participation requests reached");
        }

        RequestsEntity requestsEntity = requestsMapper.toEntity(eventId, userId);
        if (entity.getParticipantLimit() == 0 || !entity.getRequestModeration()) {
            requestsEntity.setStatus(StatusRequest.CONFIRMED);
            entity.setConfirmedRequests(entity.getConfirmedRequests() + 1);
            eventsRepository.save(entity);
        } else {
            requestsEntity.setStatus(StatusRequest.PENDING);
        }
        RequestsEntity saved = requestsRepository.save(requestsEntity);
        return requestsMapper.toDto(saved);
    }

    @Override
    public Collection<ParticipationRequestDto> getRequestsInfoByUser(Long userId) {
        if (!usersRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        return requestsRepository.findAllByUserId(userId).stream()
                .map(requestsMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto updateRequestStatus(Long userId, Long requestId) {
        RequestsEntity requestsEntity = requestsRepository.findByIdAndUserId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        if (StatusRequest.CONFIRMED.equals(requestsEntity.getStatus())) {
            EventsEntity entity = eventsRepository.findById(requestsEntity.getEventId())
                    .orElseThrow(() -> new NotFoundException("Event not found"));
            entity.setConfirmedRequests(entity.getConfirmedRequests() - 1);
            eventsRepository.save(entity);
        }

        requestsEntity.setStatus(StatusRequest.CANCELED);
        RequestsEntity saved = requestsRepository.save(requestsEntity);
        return requestsMapper.toDto(saved);
    }

}
