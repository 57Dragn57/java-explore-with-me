package ru.practicum.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.dto.*;
import ru.practicum.services.AdminService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@Validated
public class AdminController {
    private AdminService adminService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Добавление нового пользователя: {}", userDto.getEmail());
        return adminService.createUser(userDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        if (ids != null && !ids.isEmpty()) {
            log.info("Получение информации о пользователях");
            return adminService.getUsers(ids, from, size);
        }
        return List.of();
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        adminService.deleteUser(userId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Добавление категории: {}", categoryDto.getName());
        return adminService.createCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.info("Удаление категории: {}", catId);
        adminService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable long catId,
                                      @RequestBody CategoryDto categoryDto) {
        log.info("Обновление категории: {}", catId);
        return adminService.updateCategory(catId, categoryDto);
    }

    @GetMapping("/events")
    public List<EventFullDto> searchEvent(@RequestParam(required = false) List<Long> users,
                                          @RequestParam(required = false) List<String> states,
                                          @RequestParam(required = false) List<Long> categories,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_FORMAT) LocalDateTime rangeStart,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_FORMAT) LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Поиск событий");
        return adminService.searchEvent(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId, @RequestBody UpdateEventRequest eventRequest) {
        log.info("Обновление события id: {}", eventId);
        return adminService.updateEvent(eventId, eventRequest);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilation) {
        log.info("Добавление новой подборки событий");
        return adminService.createCompilation(newCompilation);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {
        log.info("Удаление подборки id: {}", compId);
        adminService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable long compId, @RequestBody UpdateCompilationRequest compilation) {
        log.info("Обновление подборки с id: {}", compId);
        return adminService.updateCompilation(compId, compilation);
    }
}
