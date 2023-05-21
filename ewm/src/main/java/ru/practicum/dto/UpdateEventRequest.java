package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Constants;
import ru.practicum.stats.StateAction;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateEventRequest {
    @Size(max = 1000)
    private String annotation;
    private long category;
    @Size(max = 3000)
    private String description;
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private int participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Size(max = 200)
    private String title;
}
