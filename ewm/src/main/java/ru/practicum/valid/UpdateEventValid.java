package ru.practicum.valid;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventRequest;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.stats.State;
import ru.practicum.stats.StateAction;

import java.time.LocalDateTime;

@UtilityClass
public class UpdateEventValid {
    public static EventFullDto valid(Event event, UpdateEventRequest eventRequest) {
        if (eventRequest.getAnnotation() != null) {
            event.setAnnotation(eventRequest.getAnnotation());
        }
        if (eventRequest.getDescription() != null) {
            event.setDescription(eventRequest.getDescription());
        }
        if (eventRequest.getEventDate() != null) {
            if (LocalDateTime.now().plusHours(1).isBefore(eventRequest.getEventDate())) {
                event.setEventDate(eventRequest.getEventDate());
            } else {
                throw new ConflictException("Event cannot start in the past");
            }
        }
        if (eventRequest.getLocation() != null) {
            event.setLon(eventRequest.getLocation().getLon());
            event.setLat(eventRequest.getLocation().getLat());
        }
        if (eventRequest.getPaid() != null) {
            event.setPaid(eventRequest.getPaid());
        }
        if (eventRequest.getParticipantLimit() != 0) {
            event.setParticipantLimit(eventRequest.getParticipantLimit());
        }
        if (eventRequest.getRequestModeration() != null) {
            event.setRequestModeration(eventRequest.getRequestModeration());
        }
        if (eventRequest.getTitle() != null) {
            event.setTitle(eventRequest.getTitle());
        }
        if (eventRequest.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(eventRequest.getStateAction().name());

            switch (stateAction) {
                case PUBLISH_EVENT:
                    if (LocalDateTime.now().plusHours(1).isBefore(event.getEventDate())) {
                        if (event.getState().equals(State.PENDING)) {
                            event.setState(State.PUBLISHED);
                            event.setPublishedOn(LocalDateTime.now());
                        } else {
                            throw new ConflictException("Event already canceled/published");
                        }
                    } else {
                        throw new ForbiddenException("Cannot publish the event because it's not in the right state: " + eventRequest.getStateAction());
                    }
                    break;
                case REJECT_EVENT:
                case CANCEL_REVIEW:
                    if (event.getState().equals(State.PUBLISHED)) {
                        throw new ConflictException("Event already published");
                    } else {
                        event.setState(State.CANCELED);
                    }
                    break;
                case SEND_TO_REVIEW:
                    if (event.getState().equals(State.PUBLISHED)) {
                        throw new ConflictException("Event already published");
                    } else {
                        event.setState(State.PENDING);
                    }
                    break;

            }
        }

        return EventMapper.toEventDto(event);
    }
}
