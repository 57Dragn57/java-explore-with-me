package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Location;
import ru.practicum.stats.StateAction;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateEventRequest {
    private String annotation;
    private long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    private StateAction state;
    private String title;
}
