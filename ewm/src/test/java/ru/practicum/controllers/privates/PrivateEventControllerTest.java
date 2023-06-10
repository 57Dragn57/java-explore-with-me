package ru.practicum.controllers.privates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.*;
import ru.practicum.services.privates.PrivateEventService;
import ru.practicum.stats.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PrivateEventController.class)
class PrivateEventControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PrivateEventService privateEventService;

    @Test
    void createEvent_whenEventIsCorrect_thenStatusIsCreated() throws Exception {
        NewEventDto newEvent = NewEventDto.builder()
                .annotation("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq")
                .description("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq")
                .eventDate(LocalDateTime.now().plusDays(5))
                .category(2)
                .location(new Location(54, 31))
                .paid(true)
                .title("tttttttttttttttttttttt")
                .participantLimit(13)
                .requestModeration(false)
                .build();

        mockMvc.perform(post("/users/{userId}/events", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createEvent_whenEventIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        NewEventDto newEvent = new NewEventDto();

        mockMvc.perform(post("/users/{userId}/events", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Mockito.verify(privateEventService, never()).createEvent(1L, newEvent);
    }

    @Test
    void getEvents_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/users/{userId}/events", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(privateEventService).getEvents(1L, 0, 10);
    }

    @Test
    void getFullEvent_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/users/{userId}/events/{eventId}", 1L, 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(privateEventService).getFullEvent(1L, 1L);
    }

    @Test
    void updateEvent_whenEventIsCorrect_thenStatusIsOk() throws Exception {
        UpdateEventRequest event = UpdateEventUserRequest.builder()
                .annotation("qwert qwert qqwert qwert")
                .description("asdsadasdsadasdasdasdasd")
                .title("TTTTTTTT")
                .build();

        long eventId = 1L;
        long userId = 1L;

        mockMvc.perform(patch("/users/{userId}/events/{eventId}", userId, eventId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getRequestEvent_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/users/{userId}/events/{eventId}/requests", 1L, 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(privateEventService).getRequestEvent(1L, 1L);
    }

    @Test
    void changeEventStatus_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        EventRequestStatusUpdateRequest event = new EventRequestStatusUpdateRequest(List.of(1L, 2L), Status.CONFIRMED);

        mockMvc.perform(patch("/users/{userId}/events/{eventId}/requests", 1L, 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(privateEventService).changeEventStatus(event, 1L, 1L);
    }

    @Test
    void changeEventStatus_whenRequestIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        EventRequestStatusUpdateRequest event = new EventRequestStatusUpdateRequest(List.of(1L, 2L), Status.CONFIRMED);

        mockMvc.perform(patch("/users/{userId}/events/{eventId}/requests", -1L, -1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(privateEventService, never()).changeEventStatus(event, -1L, -1L);
    }

    @Test
    void createComment_whenCommentIsCorrect_thenStatusIsCreated() throws Exception {
        CommentDto comment = CommentDto.builder()
                .comment("asdasdsadasdasdasd")
                .build();

        mockMvc.perform(post("/users/{userId}/events/{eventId}/comments", 1L, 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andDo(print())
                .andExpect(status().isCreated());

        Mockito.verify(privateEventService).createComment(comment, 1L, 1L);
    }

    @Test
    void createComment_whenCommentIsNotCorrect_thenStatusIsNotFound() throws Exception {
        CommentDto comment = CommentDto.builder()
                .comment("")
                .build();

        mockMvc.perform(post("/{userId}/events/{eventId}/comments", -1L, -1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andDo(print())
                .andExpect(status().isNotFound());

        Mockito.verify(privateEventService, never()).createComment(comment, -1L, -1L);
    }

    @Test
    void updateComment_whenCommentIsCorrect_thenStatusIsOk() throws Exception {
        CommentDto comment = CommentDto.builder()
                .comment("werrety rtyuutr werwe")
                .build();

        mockMvc.perform(patch("/users/{userId}/comments/{commId}", 1L, 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(privateEventService).updateComment(comment, 1L, 1L);
    }

    @Test
    void updateComment_whenCommentIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        CommentDto comment = new CommentDto();

        mockMvc.perform(patch("/users/{userId}/comments/{commId}", -1L, -1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}