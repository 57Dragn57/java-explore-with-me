package ru.practicum.controllers.publics;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.services.publics.PublicCompilationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicCompilationController.class)
class PublicCompilationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PublicCompilationService publicCompilationService;

    @Test
    void getCompilations_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/compilations")
                        .param("pinned", "false"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(publicCompilationService).getCompilations(false, 0, 10);
    }

    @Test
    void getCompilation_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/compilations/{compId}", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(publicCompilationService).getCompilation(1L);
    }
}