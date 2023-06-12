package ru.practicum.services.admins;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.repositories.CommentsRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AdminCommentService {
    private CommentsRepository commentsRepository;

    @Transactional
    public void deleteComment(long commId) {
        if (commentsRepository.existsById(commId)) {
            commentsRepository.deleteById(commId);
        } else {
            throw new NotFoundException("This comment does not exist");
        }
    }
}
