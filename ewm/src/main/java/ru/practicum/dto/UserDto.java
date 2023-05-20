package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
