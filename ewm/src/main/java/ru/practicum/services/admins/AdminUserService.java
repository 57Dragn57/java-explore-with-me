package ru.practicum.services.admins;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.UserDto;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repositories.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {
    private UserRepository userRepository;

    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByName(userDto.getName())) {
            throw new ConflictException("This username already exists");
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        if (ids.isEmpty()) {
            List<UserDto> userDtos = UserMapper.toUserDtoList(userRepository.findAll(PageRequest.of(from / size, size)).toList());
            return userDtos;
        } else {
            return UserMapper.toUserDtoList(userRepository.findAllById(ids));
        }
    }

    public UserDto getUser(long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found")));
    }

    @Transactional
    public void deleteUser(long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }
}
