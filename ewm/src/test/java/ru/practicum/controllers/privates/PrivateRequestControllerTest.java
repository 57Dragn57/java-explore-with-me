package ru.practicum.controllers.privates;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.services.privates.PrivateRequestService;

import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PrivateRequestController.class)
class PrivateRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PrivateRequestService privateRequestService;

    @Test
    void getRequests_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/users/{userId}/requests", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(privateRequestService).getRequests(1L);
    }

    @Test
    void createRequest_whenRequestIsCorrect_thenStatusIsCreated() throws Exception {
        mockMvc.perform(post("/users/{userId}/requests", 1L)
                        .param("eventId", "1"))
                .andExpect(status().isCreated())
                .andDo(print());

        Mockito.verify(privateRequestService).createRequest(1L, 1L);
    }

    @Test
    void createRequest_whenRequestIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/users/{userId}/requests", -1L)
                        .param("eventId", "1"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(privateRequestService, never()).createRequest(-1L, 1L);
    }

    @Test
    void cancelRequest_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(patch("/users/{userId}/requests/{requestId}/cancel", 1L, 1L))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(privateRequestService).cancelRequest(1L, 1L);
    }

    @Test
    void cancelRequest_whenRequestIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        mockMvc.perform(patch("/users/{userId}/requests/{requestId}/cancel", -1L, -1L))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(privateRequestService, never()).cancelRequest(-1L, -1L);
    }
}