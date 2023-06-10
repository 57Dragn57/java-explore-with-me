package ru.practicum.controllers.admins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.services.admins.AdminCompilationService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminCompilationController.class)
class AdminCompilationControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminCompilationService adminCompilationService;

    @Test
    void createCompilation_whenCompilationIsCorrect_thenStatusIsCreated() throws Exception {
        NewCompilationDto compDto = NewCompilationDto.builder()
                .events(List.of(1L, 2L, 3L))
                .pinned(true)
                .title("THE BEST")
                .build();

        mockMvc.perform(post("/admin/compilations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(compDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        Mockito.verify(adminCompilationService).createCompilation(compDto);
    }

    @Test
    void createCompilation_whenCompilationIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        NewCompilationDto compDto = NewCompilationDto.builder()
                .events(List.of())
                .pinned(true)
                .title("")
                .build();

        mockMvc.perform(post("/admin/compilations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(compDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(adminCompilationService, Mockito.never()).createCompilation(compDto);
    }

    @Test
    void updateCompilation_whenCompilationIsCorrect_thenStatusIsCreated() throws Exception {
        long compId = 1L;

        UpdateCompilationRequest compDto = UpdateCompilationRequest.builder()
                .events(List.of(1L, 2L, 3L))
                .pinned(false)
                .title("THE WORST")
                .build();

        mockMvc.perform(patch("/admin/compilations/{compId}", compId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(compDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(adminCompilationService).updateCompilation(compId, compDto);
    }

    @Test
    void updateCompilation_whenCompilationIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        long compId = -1L;

        UpdateCompilationRequest compDto = UpdateCompilationRequest.builder()
                .events(List.of())
                .pinned(false)
                .title("")
                .build();

        mockMvc.perform(patch("/admin/compilations/{compId}", compId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(compDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Mockito.verify(adminCompilationService, Mockito.never()).updateCompilation(compId, compDto);
    }
}