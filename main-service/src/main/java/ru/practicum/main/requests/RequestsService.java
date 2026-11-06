package ru.practicum.main.requests;

import ru.practicum.main.requests.dto.ParticipationRequestDto;

import java.util.Collection;

public interface RequestsService {

    ParticipationRequestDto createRequest(Long eventId, Long userId);

    Collection<ParticipationRequestDto> getRequestsInfoByUser(Long userId);

    ParticipationRequestDto updateRequestStatus(Long userId, Long requestId);

}
