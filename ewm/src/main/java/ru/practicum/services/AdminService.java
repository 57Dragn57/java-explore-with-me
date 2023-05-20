package ru.practicum.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.*;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.UserNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.repositories.CompilationRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.UserRepository;
import ru.practicum.stats.State;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.valid.UpdateEventValid.valid;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private EventRepository eventRepository;
    private CompilationRepository compilationRepository;

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

            if (userDtos.isEmpty()) {
                return List.of();
            }

            return userDtos;
        } else {
            return UserMapper.toUserDtoList(userRepository.findAllById(ids));
        }
    }

    public UserDto getUser(long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id=" + userId + " was not found")));
    }

    @Transactional
    public void deleteUser(long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("This category name already exists");
        }
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Transactional
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("This category name already exists");
        }
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Transactional
    public void deleteCategory(long catId) {
        if (categoryRepository.existsById(catId)) {
            if (!eventRepository.existsByCategoryId(catId)) {
                categoryRepository.deleteById(catId);
            } else {
                throw new ConflictException("The category is not empty");
            }
        } else {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
    }

    public List<EventFullDto> searchEvent(List<Long> users,
                                          List<String> states,
                                          List<Long> categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          int from,
                                          int size) {

        Collection<State> stateList = (states == null) ? Collections.emptyList()
                : states.stream().map(State::valueOf).collect(Collectors.toList());

        return EventMapper.toEventFullDtoList(eventRepository.searchEvents(
                users,
                stateList,
                categories,
                rangeStart,
                rangeEnd,
                PageRequest.of(from / size, size, Sort.by("eventDate"))).toList());
    }

    @Transactional
    public EventFullDto updateEvent(long eventId, UpdateEventRequest eventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (eventRequest.getCategory() != 0) {
            event.setCategory(categoryRepository.getReferenceById(eventRequest.getCategory()));
        }

        return valid(event, eventRequest);
    }

    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilation) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilation);
        if (newCompilation.getEvents() == null) {
            newCompilation.setEvents(List.of());
        }
        compilation.setEvents(eventRepository.findAllById(newCompilation.getEvents()));
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Transactional
    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest compilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation with id= " + compId + " was not found"));

        if (compilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(compilationDto.getEvents()));
        }
        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
        }

        return CompilationMapper.toCompilationDto(compilation);
    }
}
