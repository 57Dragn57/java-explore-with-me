package ru.practicum.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.*;
import ru.practicum.exceptions.*;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.RequestRepository;
import ru.practicum.stats.State;
import ru.practicum.stats.Status;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.valid.UpdateEventValid.valid;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {
    private EventRepository eventRepository;
    private CategoryService categoryService;
    private AdminService adminService;
    private RequestRepository requestRepository;

    @Transactional
    public EventFullDto createEvent(long userId, NewEventDto eventDto) {
        if (LocalDateTime.now().plusHours(2).isBefore(eventDto.getEventDate())) {
            Event event = EventMapper.toEvent(eventDto);
            event.setCategory(CategoryMapper.toCategory(categoryService.getCategory(eventDto.getCategory())));
            event.setCreatedOn(LocalDateTime.now());
            event.setInitiator(UserMapper.toUser(adminService.getUser(userId)));
            event.setState(State.PENDING);
            return EventMapper.toEventDto(eventRepository.save(event));
        }
        throw new ForbiddenException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + eventDto.getEventDate());
    }

    public List<EventShortDto> getEvents(long userId, int from, int size) {
        List<EventShortDto> eventShortDtos = EventMapper.toEventShortDtoList(eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size)).toList());

        if (eventShortDtos.isEmpty()) {
            return List.of();
        }

        return eventShortDtos;
    }

    public EventFullDto getFullEvent(long userId, long eventId) {
        EventFullDto event = EventMapper.toEventDto(eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found")));

        if (event.getInitiatior().getId() == userId) {
            return event;
        } else {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
    }

    @Transactional
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventRequest eventRequest) {
        LocalDateTime timeValid = LocalDateTime.now().plusHours(2);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (timeValid.isBefore(event.getEventDate()) &&
                !event.getState().equals(State.PUBLISHED)) {
            if (event.getInitiator().getId() == userId) {
                if (eventRequest.getCategory() != 0) {
                    event.setCategory(CategoryMapper.toCategory(categoryService.getCategory(eventRequest.getCategory())));
                }
                return valid(event, eventRequest);
            } else {
                throw new NotFoundException("Event with id=" + eventId + " was not found");
            }
        } else {
            throw new ConflictValueException("Only pending or canceled events can be changed");
        }
    }

    @Transactional
    public List<ParticipationRequestDto> getRequestEvent(long userId, long eventId) {
        List<ParticipationRequestDto> dtos = RequestMapper.toRequestDtoList(requestRepository.findAllByEventId(eventId));
        adminService.getUser(userId);

        if (dtos.isEmpty()) {
            return List.of();
        }

        return dtos;
    }

    public EventRequestStatusUpdateResult changeEventStatus(EventRequestStatusUpdateRequest eventRequest, long userId, long eventId) {
        EventRequestStatusUpdateResult eventResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> requests = RequestMapper.toRequestDtoList(requestRepository.findAllByIdIn(eventRequest.getRequestIds()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));


        if (!event.getRequestModeration() || event.getParticipantLimit() == 0 && event.getInitiator().getId() == userId) {
            return new EventRequestStatusUpdateResult();
        }

        for (ParticipationRequestDto prd : requests) {
            if (prd.getStatus().equals(Status.PENDING) && event.getConfirmedRequests() < event.getParticipantLimit()) {
                if (eventRequest.getStatus().equals(Status.CONFIRMED)) {
                    prd.setStatus(Status.CONFIRMED);
                    eventResult.getConfirmedRequests().add(prd);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else {
                    prd.setStatus(Status.REJECTED);
                    eventResult.getRejectedRequests().add(prd);
                }
            } else {
                throw new ConflictException("The participant limit has been reached");
            }
        }
        return eventResult;
    }

    public List<ParticipationRequestDto> getRequests(long userId) {
        adminService.getUser(userId);
        List<ParticipationRequestDto> requestDtos = RequestMapper.toRequestDtoList(requestRepository.findByUserId(userId));

        if (requestDtos == null) {
            return List.of();
        }

        return requestDtos;
    }

    @Transactional
    public ParticipationRequestDto createRequest(long userId, long eventId) {
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .user(UserMapper.toUser(adminService.getUser(userId)))
                .event(eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found")))
                .build();

        if (requestRepository.existsByUserId(userId) &&
                request.getEvent().getInitiator().getId() != userId &&
                request.getEvent().getState() == State.PUBLISHED) {
            if (request.getEvent().getParticipantLimit() == 0 ||
                    request.getEvent().getParticipantLimit() > request.getEvent().getConfirmedRequests()) {
                if (!request.getEvent().getRequestModeration()) {
                    request.setStatus(Status.CONFIRMED);
                } else {
                    request.setStatus(Status.PENDING);
                }
            } else {
                throw new ConflictException("Your request is not validated");
            }


        } else {
            throw new ConflictException("Your request is not validated");
        }

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
        if (request.getUser().getId() == userId) {
            request.setStatus(Status.REJECTED);
            return RequestMapper.toRequestDto(request);
        } else {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }

    }
}
