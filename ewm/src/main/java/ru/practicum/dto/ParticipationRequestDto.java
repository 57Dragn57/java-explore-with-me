package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.stats.Status;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ParticipationRequestDto {
    private LocalDateTime created;
    private long event;
    private long id;
    private long requester;
    private Status status;
}
