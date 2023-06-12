package ru.practicum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Comment;
import ru.practicum.model.CommentCountProjection;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT new ru.practicum.model.CommentCountProjection(c.event.id, COUNT(c)) FROM Comment c GROUP BY c.event.id")
    List<CommentCountProjection> findCommentCountsByEvent();

    Page<Comment> findByEventId(long eventId, Pageable pageable);
}

