package ru.practicum.services.publics;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.CommentCountProjection;
import ru.practicum.model.Event;
import ru.practicum.repositories.CommentsRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.stats.State;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PublicCommentService {
    private CommentsRepository commentsRepository;
    private EventRepository eventRepository;

    public CommentDto getComment(long commId, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id:" + eventId + " was now found"));
        if (event.getState().equals(State.PUBLISHED)) {
            return CommentMapper.toCommentDto(commentsRepository.findById(commId).orElseThrow(() -> new NotFoundException("Comment with id=" + commId + " was not found")));
        } else {
            throw new ConflictException("You do not have access to this event");
        }
    }

    public Set<CommentDto> getComments(long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id:" + eventId + " was now found"));
        if (event.getState().equals(State.PUBLISHED)) {
            return CommentMapper.toCommentsDtoSet(commentsRepository.findByEventId(eventId, PageRequest.of(from / size, size, Sort.by(DESC, "createDate"))).toSet());
        } else {
            throw new ConflictException("You do not have access to this event");
        }
    }

    public List<EventFullDto> findCommentCountsByEvent(List<EventFullDto> events) {
        Map<Long, Long> countByEvent = commentsRepository.findCommentCountsByEvent()
                .stream().collect(Collectors.toMap(CommentCountProjection::getEventId, CommentCountProjection::getCount));

        events.forEach(event -> {
            Long commentCount = countByEvent.get(event.getId());
            if (commentCount != null) {
                event.setCountComments(commentCount);
            }
        });

        return events;
    }
}
