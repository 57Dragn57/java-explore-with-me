package ru.practicum.valid;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventRequest;
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
            event.setEventDate(eventRequest.getEventDate());
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
        if (eventRequest.getState() != null) {
            StateAction stateAction = StateAction.valueOf(eventRequest.getState().name());

            switch (stateAction) {
                case PUBLISH_EVENT:
                    if (LocalDateTime.now().plusHours(1).isBefore(event.getEventDate())) {
                        event.setState(State.PUBLISHED);
                    } else {
                        throw new ForbiddenException("Cannot publish the event because it's not in the right state: " + eventRequest.getState());
                    }

                case REJECT_EVENT:
                    if (event.getState().equals(State.PUBLISHED)) {
                        throw new ForbiddenException("Cannot publish the event because it's not in the right state: " + State.PUBLISHED);
                    } else {
                        event.setState(State.CANCELED);
                    }

                case SEND_TO_REVIEW:


                case CANCEL_REVIEW:

                    break;
            }
        }

        return EventMapper.toEventDto(event);
    }
}
