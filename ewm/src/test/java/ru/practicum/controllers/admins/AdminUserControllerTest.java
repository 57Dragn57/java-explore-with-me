package ru.practicum.controllers.admins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.UserDto;
import ru.practicum.services.admins.AdminUserService;

import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminUserController.class)
class AdminUserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminUserService adminUserService;

    @Test
    void createUser_whenUserIsCorrect_thenStatusIsOk() throws Exception {
        UserDto user = UserDto.builder()
                .id(0)
                .name("Michael")
                .email("michael@ya.ru")
                .build();

        mockMvc.perform(post("/admin/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isCreated());

        Mockito.verify(adminUserService).createUser(user);
    }

    @Test
    void createUser_whenUserIsNotCorrect_thenStatusIsBadRequest() throws Exception {
        UserDto user = UserDto.builder()
                .id(0)
                .name("")
                .email("")
                .build();

        mockMvc.perform(post("/admin/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Mockito.verify(adminUserService, never()).createUser(user);
    }

    @Test
    void getUser_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(adminUserService).getUsers(null, 0, 10);
    }
}