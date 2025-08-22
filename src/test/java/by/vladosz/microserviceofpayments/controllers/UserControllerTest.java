package by.vladosz.microserviceofpayments.controllers;

import by.vladosz.microserviceofpayments.dto.UserDTO;
import by.vladosz.microserviceofpayments.models.User;
import by.vladosz.microserviceofpayments.models.UserRole;
import by.vladosz.microserviceofpayments.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Успешный тест на вывод все пользователей")
    void findAllUsers() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;

        UserDTO userDTO1 = new UserDTO(
                "Ivan",
                "Shimdt",
                "Andreevich",
                40
        );

        UserDTO userDTO2 = new UserDTO(
                "Anna",
                "Smirnova",
                "Ivanovna",
                40
        );

        Page<UserDTO> mockPage = new PageImpl<>(List.of(userDTO1, userDTO2));

        when(userService.findAll(pageNumber, pageSize)).thenReturn(mockPage);

        mockMvc.perform(
                get("/api/users")
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize))
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("Успешный тест на создание пользователя")
    void createUserTest() throws Exception {
        UserRole userRole = new UserRole();
        userRole.setId(1);
        userRole.setName("ROLE_USER");
        Collection<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        User user = new User(1L, "Иван", "Петров", "Сергеевич", 25,
                "ivan.petrov@example.com", "securePass123", userRoles);

        UserDTO userDTO = new UserDTO( "Иван", "Петров", "Сергеевич", 25);

        String userJson = objectMapper.writeValueAsString(user);

        when(userService.createUser(any(User.class)))
                .thenReturn(ResponseEntity.ok(userDTO));

        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName").value("Иван"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @DisplayName("Тест провален над созданием пустого пользователя")
    void createUserFound() throws Exception {
        mockMvc.perform(
                post("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Успешный тест на нахождения пользователя по id")
    void findUserByIdTest() throws Exception {
        UserRole userRole = new UserRole();
        userRole.setId(1);
        userRole.setName("ROLE_USER");
        Collection<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        User user = new User(1L, "Иван", "Петров", "Сергеевич", 25,
                "ivan.petrov@example.com", "securePass123", userRoles);

        UserDTO userDTO = new UserDTO( "Иван", "Петров", "Сергеевич", 25);

        String userJson = objectMapper.writeValueAsString(user);

        when(userService.findUserById(user.getId())).thenReturn(userDTO);

        mockMvc.perform(
                        get("/api/users/{id}", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Иван"));

        verify(userService, times(1)).findUserById(user.getId());
    }

    @Test
    @DisplayName("Успешный тест на удаление пользователя по id")
    void findUserByIdFound() throws Exception {
        Long userId = 1L;

        mockMvc.perform(
                delete("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }





}
