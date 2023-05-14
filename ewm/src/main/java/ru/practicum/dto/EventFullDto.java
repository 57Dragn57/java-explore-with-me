package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Location;
import ru.practicum.stats.State;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventFullDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    @NotNull
    @Future
    private LocalDateTime eventDate;
    private long id;
    @NotNull
    private UserShortDto initiatior;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    private int participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    @NotBlank
    private String title;
    private int views;
}
