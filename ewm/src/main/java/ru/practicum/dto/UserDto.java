package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {
    private long id;
    @Email
    @NotBlank
    @Size(min = 5, max = 55)
    private String email;
    @NotBlank
    @Size(min = 5, max = 55)
    private String name;
}
