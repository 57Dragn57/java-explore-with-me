package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }


}
