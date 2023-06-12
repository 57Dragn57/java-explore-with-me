package ru.practicum.services.privates;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.repositories.CommentsRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.services.admins.AdminUserService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PrivateCommentService {
    private AdminUserService adminService;
    private EventRepository eventRepository;
    private CommentsRepository commentsRepository;

    @Transactional
    public CommentDto createComment(NewCommentDto commentDto, long eventId, long userId) {
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setCommentator(UserMapper.toUser(adminService.getUser(userId)));
        comment.setCreateDate(LocalDateTime.now());
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        comment.setEvent(event);

        return CommentMapper.toCommentDto(commentsRepository.save(comment));
    }

    @Transactional
    public CommentDto updateComment(NewCommentDto commentDto, long commId, long userId) {
        LocalDateTime now = LocalDateTime.now();
        Comment comment = commentsRepository.findById(commId).orElseThrow(() -> new NotFoundException("Comment with id=" + commId + " was not found"));

        if ((comment.getCommentator().getId() == userId) && (comment.getCreateDate().plusHours(1).isAfter(now))) {
            comment.setComment(commentDto.getComment());
            comment.setLastUpdate(now);
            return CommentMapper.toCommentDto(comment);
        } else {
            throw new ValidationException("You cannot edit this comment");
        }
    }

    @Transactional
    public void deleteComment(long commId, long userId) {
        Comment comment = commentsRepository.findById(commId).orElseThrow(() -> new NotFoundException("Comment with id=" + commId + " was not found"));
        if (comment.getCommentator().getId() == userId) {
            commentsRepository.deleteById(commId);
        } else {
            throw new ValidationException("You cannot delete this comment");
        }
    }
}
