package ru.practicum.services.publics;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Constants;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.explore.stats.HitDto;
import ru.practicum.explore.stats.StatsClient;
import ru.practicum.explore.stats.VisitDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.repositories.EventRepository;
import ru.practicum.stats.Sorted;
import ru.practicum.stats.State;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PublicEventService {
    private EventRepository eventRepository;
    private StatsClient statsClient;

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
        List<EventShortDto> events = EventMapper.toEventShortDtoList(eventRepository.findEventsByFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable));

        if (events.isEmpty()) {
            return List.of();
        }

        String minEventDate = String.format(events.stream().map(EventShortDto::getEventDate)
                .min(Comparator.naturalOrder())
                .orElse(Constants.START), Constants.DATE_FORMAT);

        List<String> uris = events.stream()
                .map(event -> "event/" + event.getId())
                .collect(Collectors.toList());

        List<VisitDto> visits = visits(minEventDate, uris);
        events.stream()
                .flatMap(event -> visits.stream()
                        .filter(visit -> visit.getUri().endsWith("/" + event.getId()))
                        .map(visit -> {
                            event.setViews(visit.getHits());
                            return event;
                        }))
                .distinct()
                .collect(Collectors.toList());

        if (sort != null) {
            if (Sorted.EVENT_DATE.toString().equals(sort)) {
                events.sort(Comparator.comparing(EventShortDto::getEventDate).reversed());
            } else if (Sorted.VIEWS.toString().equals(sort)) {
                events.sort(Comparator.comparing(EventShortDto::getViews).reversed());
            }
        } else {
            events.sort(Comparator.comparing(EventShortDto::getId).reversed());
        }

        Pageable pageable = PageRequest.of(from, size);

        List<EventShortDto> pagedEvents = events
                .stream()
                .skip((long) from * (long) size)
                .limit(size)
                .collect(Collectors.toList());

        Page<EventShortDto> page = new PageImpl<>(pagedEvents, pageable, events.size());

        addHit(new HitDto(Constants.APP_NAME, endpoint, ip, LocalDateTime.now()));
        return page.getContent();
    }

    public EventFullDto getEventById(long id,
                                     String ip,
                                     String endpoint) {
        EventFullDto event = EventMapper.toEventDto(eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found")));

        if (event.getState().equals(State.PUBLISHED)) {

            String start = String.format(event.getEventDate(), Constants.FORMATTER);

            for (VisitDto vd : visits(start, List.of(endpoint))) {
                if (vd.getUri().contains(String.valueOf(event.getId()))) {
                    event.setViews(vd.getHits());
                }
            }

            addHit(new HitDto(Constants.APP_NAME, endpoint, ip, LocalDateTime.now()));
            return event;
        }
        throw new NotFoundException("Event with id=" + id + " was not found");
    }

    private void addHit(HitDto hitDto) {
        statsClient.addHit(hitDto);
    }

    private List<VisitDto> visits(String start, List<String> uris) {
        return statsClient.getStats(start, Constants.END, uris, true);
    }
}
