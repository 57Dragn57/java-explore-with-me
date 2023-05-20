package ru.practicum.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
@Validated
public class UserController {
    private UserService userService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Positive @PathVariable long userId,
                                    @Valid @RequestBody NewEventDto eventDto) {
        log.info("Добавление события name: {}", eventDto.getTitle());
        return userService.createEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(@Positive @PathVariable long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Получение списка событий пользователя id: {}", userId);
        return userService.getEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getFullEvent(@Positive @PathVariable long userId,
                                     @PathVariable long eventId) {
        log.info("Получение полной информации о событии id: {}", eventId);
        return userService.getFullEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@Positive @PathVariable long userId,
                                    @Positive @PathVariable long eventId,
                                    @Valid @RequestBody UpdateEventRequest eventRequest) {
        log.info("Изменение параметров события id: {}", eventId);
        return userService.updateEvent(userId, eventId, eventRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestEvent(@Positive @PathVariable long userId,
                                                         @Positive @PathVariable long eventId) {
        log.info("Получение информации о запросах на участие в событии id: {}", eventId);
        return userService.getRequestEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeEventStatus(@Valid @RequestBody EventRequestStatusUpdateRequest eventRequest,
                                                            @Positive @PathVariable long userId,
                                                            @Positive @PathVariable long eventId) {
        log.info("Изменение статуса запроса на участие в событии id: {}", eventId);
        return userService.changeEventStatus(eventRequest, userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@Positive @PathVariable long userId) {
        log.info("Получение информации о заявках текущего пользователя id: {}", userId);
        return userService.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@Positive @PathVariable long userId,
                                                 @Positive @RequestParam long eventId) {
        log.info("Добавление запроса на участие пользователем id: {}", userId);
        return userService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@Positive @PathVariable long userId,
                                                 @Positive @PathVariable long requestId) {
        log.info("Отмена запроса на участие в событии id: {}", requestId);
        return userService.cancelRequest(userId, requestId);
    }
}
