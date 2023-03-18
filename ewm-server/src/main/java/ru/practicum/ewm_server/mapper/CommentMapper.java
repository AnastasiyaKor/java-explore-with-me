package ru.practicum.ewm_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_server.dto.CommentFullDto;
import ru.practicum.ewm_server.dto.CommentShortDto;
import ru.practicum.ewm_server.dto.CommentStatusDto;
import ru.practicum.ewm_server.dto.UserShortDto;
import ru.practicum.ewm_server.entity.Comment;
import ru.practicum.ewm_server.entity.CommentsAdmin;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public static CommentFullDto toCommentFullDto(Comment comment) {
        return new CommentFullDto(
                comment.getId(),
                comment.getComment(),
                new UserShortDto(comment.getCommentator().getId(), comment.getCommentator().getName()),
                comment.getCommentedEvent().getId(),
                comment.getCreateOn(),
                comment.getPublishedOn(),
                comment.getEditOn() != null ? comment.getEditOn() : null,
                comment.getStatus()
        );
    }


    public static CommentStatusDto toCommentStatusDto(Comment comment, CommentsAdmin commentsAdmin) {
        return new CommentStatusDto(
                comment.getId(),
                comment.getComment(),
                new UserShortDto(comment.getCommentator().getId(), comment.getCommentator().getName()),
                comment.getCommentedEvent().getId(),
                comment.getCreateOn(),
                comment.getPublishedOn(),
                comment.getEditOn() != null ? comment.getEditOn() : null,
                comment.getStatus(),
                commentsAdmin != null ? commentsAdmin.getCommentAdmin() : null,
                commentsAdmin != null ? commentsAdmin.getCreatedOn() : null
        );
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return new CommentShortDto(
                comment.getId(),
                comment.getComment(),
                new UserShortDto(comment.getCommentator().getId(), comment.getCommentator().getName()),
                comment.getPublishedOn()
        );
    }

    public static List<CommentShortDto> toListCommentShortDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentShortDto)
                .collect(Collectors.toList());
    }

    public static List<CommentFullDto> toListCommentFullDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }
}
