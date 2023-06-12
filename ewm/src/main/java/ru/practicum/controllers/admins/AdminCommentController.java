package ru.practicum.controllers.admins;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.services.admins.AdminCommentService;

import javax.validation.constraints.Positive;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@Validated
public class AdminCommentController {
    private AdminCommentService adminCommentService;

    @DeleteMapping("/comments/{commId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Positive @PathVariable long commId) {
        log.info("Удаление комментария с id:{}", commId);
        adminCommentService.deleteComment(commId);
    }
}
