package ru.practicum.controllers.publics;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CommentDto;
import ru.practicum.services.publics.PublicCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Set;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/events")
@Validated
public class PublicCommentController {
    private PublicCommentService publicCommentService;

    @GetMapping("/{eventId}/comments/{commId}")
    public CommentDto getComment(@Positive @PathVariable long eventId,
                                 @Positive @PathVariable long commId) {
        log.info("Процесс получения комментария с id:{}", commId);
        return publicCommentService.getComment(commId, eventId);
    }

    @GetMapping("/{eventId}/comments")
    public Set<CommentDto> getComment(@Positive @PathVariable long eventId,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Процесс получения комментариев события с id:{}", eventId);
        return publicCommentService.getComments(eventId, from, size);
    }
}
