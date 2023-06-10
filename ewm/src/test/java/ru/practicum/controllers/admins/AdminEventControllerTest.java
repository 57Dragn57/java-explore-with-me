package ru.practicum.controllers.admins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.Location;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.dto.UpdateEventRequest;
import ru.practicum.services.admins.AdminEventService;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminEventController.class)
class AdminEventControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminEventService adminEventService;

    @Test
    void searchEvent_thenStatusIsOk() throws Exception{
        mockMvc.perform(get("/admin/events")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print());


        Mockito.verify(adminEventService).searchEvent(null, null, null, null, null, 0, 10);
    }

    @Test
    void updateEvent_whenEventIsCorrect_thenStatusIsOk() throws Exception{
        UpdateEventRequest event = UpdateEventAdminRequest.builder()
                .annotation("qwert qwert qqwert qwert")
                .description("asdsadasdsadasdasdasdasd")
                .title("TTTTTTTT")
                .build();
        long eventId = 1L;

        mockMvc.perform(patch("/admin/events/{eventId}", eventId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateEvent_whenEventIsNotCorrect_thenStatusIsBadRequest() throws Exception{
        UpdateEventRequest event = UpdateEventAdminRequest.builder()
                .annotation("")
                .description("")
                .title("")
                .build();
        long eventId = 1L;

        mockMvc.perform(patch("/admin/events/{eventId}", eventId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}