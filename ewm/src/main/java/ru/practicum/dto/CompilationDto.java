package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompilationDto {
    private List<EventShortDto> events;
    @Positive
    private long id;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
}
