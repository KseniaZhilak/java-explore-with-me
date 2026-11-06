package ru.practicum.main.comments;

import ru.practicum.main.comments.dto.CommentsDto;
import ru.practicum.main.comments.dto.CommentsRequestDto;
import ru.practicum.main.comments.dto.CommentsUpdateDto;

import java.util.Collection;

public interface CommentsService {

    CommentsDto createComment(Long eventId, Long userId, CommentsRequestDto commentsRequestDto);

    CommentsDto getCommentById(Long commentId);

    Collection<CommentsDto> getCommentsByEventId(Long eventId, int from, int size);

    CommentsDto updateComment(Long userId, Long commentId, CommentsUpdateDto commentsUpdateDto);

    void deleteComment(Long userId, Long commentId);

}
