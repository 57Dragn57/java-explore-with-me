package ru.practicum.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.repositories.EventRepository;
import ru.practicum.stats.Sorted;
import ru.practicum.stats.State;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class EventService {
    private EventRepository eventRepository;

    public List<EventShortDto> getEventByFilters(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable,
                                                 String sort,
                                                 int from,
                                                 int size,
                                                 String ip,
                                                 String endpoint) {
        Sort sorted = null;
        List<EventShortDto> events;
        if (sort != null) {
            if (Sorted.EVENT_DATE.toString().equals(sort)) {
                sorted = Sort.by("eventDate");
            } else if (Sorted.VIEWS.toString().equals(sort)) {
                sorted = Sort.by(DESC, "views");
            }
        }
        events = EventMapper.toEventShortDtoList(eventRepository.findEventsByFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, PageRequest.of(from / size, size, sorted)).toList());

        if (events == null || events.isEmpty()) {
            return List.of();
        }

        return events;
    }

    public EventFullDto getEventById(long id) {
        EventFullDto event = EventMapper.toEventDto(eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found")));

        if (event.getState().equals(State.PUBLISHED)) {
            return event;
        }
        throw new NotFoundException("Event with id=" + id + " was not found");
    }
}
