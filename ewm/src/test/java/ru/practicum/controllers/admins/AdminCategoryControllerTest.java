package ru.practicum.controllers.admins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.CategoryDto;
import ru.practicum.services.admins.AdminCategoryService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminCategoryController.class)
class AdminCategoryControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminCategoryService adminCategoryService;

    @Test
    void addCategory_whenCategoryIsCorrect_thenStatusIsCreated() throws Exception {
        CategoryDto catDto = CategoryDto.builder()
                .id(0)
                .name("Concert")
                .build();

        mockMvc.perform(post("/admin/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(catDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        Mockito.verify(adminCategoryService).createCategory(catDto);
    }

    @Test
    void addCategory_whenCategoryIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        CategoryDto catDto = CategoryDto.builder()
                .id(0)
                .name("")
                .build();

        mockMvc.perform(post("/admin/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(catDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(adminCategoryService, Mockito.never()).createCategory(catDto);
    }

    @Test
    void updateCategory_whenCategoryIsCorrect_thenStatusIsOk() throws Exception {
        long catId = 1L;

        CategoryDto catDto = CategoryDto.builder()
                .id(0)
                .name("Fishing")
                .build();

        mockMvc.perform(patch("/admin/categories/{catId}", catId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(catDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(adminCategoryService).updateCategory(catId, catDto);
    }

    @Test
    void updateCategory_whenCategoryIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        long catId = -1L;

        CategoryDto catDto = CategoryDto.builder()
                .id(0)
                .name("Fishing")
                .build();

        mockMvc.perform(patch("/admin/categories/{catId}", catId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(catDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Mockito.verify(adminCategoryService, Mockito.never()).updateCategory(catId, catDto);
    }
}