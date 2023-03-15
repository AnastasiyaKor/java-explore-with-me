package ru.practicum.ewm_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_server.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "select c from Comment  as c where c.id = ?1 and c.commentator.id = ?2")
    Comment getCommentById(int commentId, int userId);

    @Query(value = "select c from Comment as c where c.commentator.id = ?1 order by c.createOn DESC")
    Page<Comment> getComments(int userId, Pageable pageable);

    @Query(value = "select c from Comment  as c where c.commentedEvent.id = ?1 " +
            " and c.status = 'PUBLISHED' order by c.publishedOn DESC ")
    List<Comment> getCommentByEventId(int eventId);
}
