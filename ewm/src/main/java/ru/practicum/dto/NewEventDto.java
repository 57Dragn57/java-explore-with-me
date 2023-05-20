package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Constants;
import ru.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    @NotBlank
    private String title;

    public void setEventDate(String date) {
        this.eventDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }
}
