package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    private String annotation;
    @Positive
    private int category;
    @NotBlank
    private String description;
    @Future
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    @NotBlank
    private String title;
}
