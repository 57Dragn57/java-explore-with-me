package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private LocalDateTime eventDate;
    private long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private int views;
}
