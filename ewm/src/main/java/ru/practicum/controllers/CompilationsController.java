package ru.practicum.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.services.CompilationService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/compilations")
@Validated
public class CompilationsController {
    private CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Получение списка подборок");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable long compId) {
        log.info("Получение подборки по id: {}", compId);
        return compilationService.getCompilation(compId);
    }
}
