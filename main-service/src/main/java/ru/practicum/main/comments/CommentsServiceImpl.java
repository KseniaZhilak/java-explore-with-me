package ru.practicum.main.comments;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.comments.dto.CommentsDto;
import ru.practicum.main.comments.dto.CommentsRequestDto;
import ru.practicum.main.comments.dto.CommentsUpdateDto;
import ru.practicum.main.comments.repository.CommentsEntity;
import ru.practicum.main.comments.repository.CommentsRepository;
import ru.practicum.main.comments.repository.mapper.CommentsMapper;
import ru.practicum.main.events.repository.EventsEntity;
import ru.practicum.main.events.repository.EventsRepository;
import ru.practicum.main.events.states.State;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.users.repository.UsersRepository;

import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final CommentsMapper commentsMapper;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;

    public CommentsServiceImpl(CommentsRepository commentsRepository, CommentsMapper commentsMapper,
                               EventsRepository eventsRepository, UsersRepository usersRepository) {
        this.commentsRepository = commentsRepository;
        this.commentsMapper = commentsMapper;
        this.eventsRepository = eventsRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional
    public CommentsDto createComment(Long eventId, Long userId, CommentsRequestDto commentsRequestDto) {
        EventsEntity event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        if (!State.PUBLISHED.equals(event.getState())) {
            throw new ConflictException("Event is not published");
        }
        if (!usersRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        CommentsEntity entity = commentsMapper.toEntity(eventId, userId, commentsRequestDto);
        CommentsEntity saved = commentsRepository.save(entity);
        return commentsMapper.toDto(saved);
    }

    @Override
    public CommentsDto getCommentById(Long commentId) {
        CommentsEntity entity = commentsRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment not found")
        );
        return commentsMapper.toDto(entity);
    }

    @Override
    public List<CommentsDto> getCommentsByEventId(Long eventId, int from, int size) {
        if (!eventsRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found");
        }
        return commentsRepository
                .findAllByEventId(eventId, from, size).stream().map(commentsMapper::toDto).toList();
    }

    @Override
    @Transactional
    public CommentsDto updateComment(Long userId,
                                     Long commentId, CommentsUpdateDto commentsUpdateDto) {
        CommentsEntity entity = commentsRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        commentsMapper.setCommentsUpdateDto(entity, commentsUpdateDto);
        CommentsEntity saved = commentsRepository.save(entity);
        return commentsMapper.toDto(saved);

    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        CommentsEntity entity = commentsRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        commentsRepository.deleteById(entity.getId());
    }
}
