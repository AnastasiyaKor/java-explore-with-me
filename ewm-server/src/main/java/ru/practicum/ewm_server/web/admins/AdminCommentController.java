package ru.practicum.ewm_server.web.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.CommentFullDto;
import ru.practicum.ewm_server.dto.UpdateStatusAdminComments;
import ru.practicum.ewm_server.entity.Comment;
import ru.practicum.ewm_server.mapper.CommentMapper;
import ru.practicum.ewm_server.service.CommentService;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentFullDto updateCommentStatus(@RequestBody @Valid UpdateStatusAdminComments updateStatusAdminComments,
                                              @PathVariable int commentId) {
        log.info("Received a request from the administrator to update the status of the comment the id:{}", commentId);
        commentService.getById(commentId);
        Comment comment = commentService.updateCommentStatus(updateStatusAdminComments, commentId);
        return CommentMapper.toCommentFullDto(comment);
    }
}
