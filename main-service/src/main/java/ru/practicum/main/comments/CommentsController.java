package ru.practicum.main.comments;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comments.dto.CommentsDto;
import ru.practicum.main.comments.dto.CommentsRequestDto;
import ru.practicum.main.comments.dto.CommentsUpdateDto;

import java.util.Collection;

@RestController
@Validated
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public CommentsDto createComment(
            @PathVariable Long eventId,
            @PathVariable Long userId,
            @RequestBody @Valid CommentsRequestDto commentsRequestDto) {
        return commentsService.createComment(eventId, userId, commentsRequestDto);
    }

    @PatchMapping("/users/{userId}/comments/{id}")
    public CommentsDto updateComment(
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestBody @Valid CommentsUpdateDto commentsUpdateDto) {
        return commentsService.updateComment(userId, id, commentsUpdateDto);
    }

    @GetMapping("/events/{eventId}/comments")
    public Collection<CommentsDto> getCommentsByEventId(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        return commentsService.getCommentsByEventId(eventId, from, size);
    }

    @GetMapping("/comments/{commentId}")
    public CommentsDto getCommentById(@PathVariable Long commentId) {
        return commentsService.getCommentById(commentId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}/comments/{id}")
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long id) {
        commentsService.deleteComment(userId, id);
    }

}
