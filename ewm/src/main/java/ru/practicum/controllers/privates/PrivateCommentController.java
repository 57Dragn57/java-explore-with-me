package ru.practicum.controllers.privates;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.services.privates.PrivateCommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
@Validated
public class PrivateCommentController {
    private PrivateCommentService privateCommentService;

    @PostMapping("/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@Valid @RequestBody NewCommentDto commentDto,
                                    @Positive @PathVariable long eventId,
                                    @Positive @PathVariable long userId) {
        log.info("Добавление сообщения в событие с id:{}", eventId);
        return privateCommentService.createComment(commentDto, eventId, userId);
    }

    @PatchMapping("/{userId}/comments/{commId}")
    public CommentDto updateComment(@Valid @RequestBody NewCommentDto commentDto,
                                    @Positive @PathVariable long commId,
                                    @Positive @PathVariable long userId) {
        log.info("Обновление сообщения с id:{}", commId);
        return privateCommentService.updateComment(commentDto, commId, userId);
    }

    @DeleteMapping("/{userId}/comments/{commId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Positive @PathVariable long commId,
                              @Positive @PathVariable long userId) {
        log.info("Удаление комментария id:{}", commId);
        privateCommentService.deleteComment(commId, userId);
    }
}
