package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(max = 1000)
    private String annotation;
    @Positive
    private long category;
    @NotBlank
    @Size(max = 3000)
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private boolean paid;
    @PositiveOrZero
    private int participantLimit;
    private boolean requestModeration;
    @NotBlank
    @Size(max = 200)
    private String title;

    public void setRequestModeration(Boolean rm) {
        this.requestModeration = Objects.requireNonNullElse(rm, true);
    }

    public boolean getRequestModeration() {
        return requestModeration;
    }
}
