package ru.practicum.ewm_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_server.entity.CommentsAdmin;

public interface CommentsAdminRepository extends JpaRepository<CommentsAdmin, Integer> {

    @Query(value = "select ca from CommentsAdmin as ca where ca.commentId.id = ?1")
    CommentsAdmin getCommentAdmin(int commentId);
}
