package ru.practicum.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.services.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/events")
@Validated
public class EventController {
    private EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsByFilters(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) LocalDateTime rangeStart,
                                                  @RequestParam(required = false) LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        log.info("Поиск событий по фильтрам");
        return eventService.getEventByFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable long id) {
        log.info("Получить событие по его id: {}", id);
        return eventService.getEventById(id);
    }
}
