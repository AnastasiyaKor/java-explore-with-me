package ru.practicum.ewm_server.web.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.CommentFullDto;
import ru.practicum.ewm_server.dto.CommentStatusDto;
import ru.practicum.ewm_server.dto.NewCommentDto;
import ru.practicum.ewm_server.dto.UpdateCommentDto;
import ru.practicum.ewm_server.entity.Comment;
import ru.practicum.ewm_server.mapper.CommentMapper;
import ru.practicum.ewm_server.service.CommentService;
import ru.practicum.ewm_server.service.EventService;
import ru.practicum.ewm_server.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto addComment(@RequestBody @Valid NewCommentDto newCommentDto, @PathVariable int userId,
                                     @RequestParam int eventId) {
        log.info("A request was received from the user under the ID: {} " +
                "to create a comment on the event under the ID: {}", userId, eventId);
        userService.getById(userId);
        eventService.getById(eventId);
        Comment comment = commentService.add(newCommentDto, userId, eventId);
        return CommentMapper.toCommentFullDto(comment);
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto update(@RequestBody @Valid UpdateCommentDto updateCommentDto,
                                 @PathVariable int userId, @PathVariable int commentId) {
        log.info("A request was received from the user under the ID: {}  " +
                "to update the comment on the event under the ID: {} ", userId, commentId);
        userService.getById(userId);
        commentService.getById(commentId);
        Comment comment = commentService.update(updateCommentDto, userId, commentId);
        return CommentMapper.toCommentFullDto(comment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int userId, @PathVariable int commentId) {
        log.info("Received a comment from the user under the ID: {} to delete the user", userId);
        userService.getById(userId);
        commentService.getById(commentId);
        commentService.delete(userId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentStatusDto getCommentStatus(@PathVariable int userId, @PathVariable int commentId) {
        log.info("A request was received from a user under the ID: {}, " +
                "to receive the status of a comment under the ID: {}", userId, commentId);
        userService.getById(userId);
        commentService.getById(commentId);
        return commentService.getCommentStatus(userId, commentId);
    }

    @GetMapping
    public List<CommentFullDto> getComments(@PathVariable int userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("A request was received from a user under the ID: {}, to receive their comments", userId);
        userService.getById(userId);
        List<Comment> comments = commentService.getComments(userId, from, size);
        return CommentMapper.toListCommentFullDto(comments);
    }
}
