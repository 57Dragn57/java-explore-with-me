package ru.practicum.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.services.UserService;

import javax.validation.Valid;
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
    public EventFullDto createEvent(@PathVariable long userId,
                                    @Valid @RequestBody NewEventDto eventDto) {
        log.info("Добавление события name: {}", eventDto.getTitle());
        return userService.createEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Получение списка событий пользователя id: {}", userId);
        return userService.getEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getFullEvent(@PathVariable long userId,
                                     @PathVariable long eventId) {
        log.info("Получение полной информации о событии id: {}", eventId);
        return userService.getFullEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @RequestBody UpdateEventRequest eventRequest) {
        log.info("Изменение параметров события id: {}", eventId);
        return userService.updateEvent(userId, eventId, eventRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestEvent(@PathVariable long userId,
                                                         @PathVariable long eventId) {
        log.info("Получение информации о запросах на участие в событии id: {}", eventId);
        return userService.getRequestEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeEventStatus(@RequestBody EventRequestStatusUpdateRequest eventRequest,
                                                            @PathVariable long userId,
                                                            @PathVariable long eventId) {
        log.info("Изменение статуса запроса на участие в событии id: {}", eventId);
        return userService.changeEventStatus(eventRequest, userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId) {
        log.info("Получение информации о заявках текущего пользователя id: {}", userId);
        return userService.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable long userId,
                                                 @RequestParam long eventId) {
        log.info("Добавление запроса на участие пользователем id: {}", userId);
        return userService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId,
                                                 @PathVariable long requestId) {
        log.info("Отмена запроса на участие в событии id: {}", requestId);
        return userService.cancelRequest(userId, requestId);
    }
}
