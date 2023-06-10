package ru.practicum.controllers.publics;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.services.publics.PublicCategoryService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicCategoryController.class)
class PublicCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PublicCategoryService publicCategoryService;

    @Test
    void getCategories_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/categories/"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(publicCategoryService).getCategories(0, 10);
    }

    @Test
    void getCategory_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/categories/{catId}", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(publicCategoryService).getCategory(1L);
    }
}