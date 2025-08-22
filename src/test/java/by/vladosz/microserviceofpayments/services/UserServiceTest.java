package by.vladosz.microserviceofpayments.services;

import by.vladosz.microserviceofpayments.dto.UserDTO;
import by.vladosz.microserviceofpayments.mappers.UserMapper;
import by.vladosz.microserviceofpayments.models.User;
import by.vladosz.microserviceofpayments.models.UserRole;
import by.vladosz.microserviceofpayments.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Правильный тест на вывод всех пользователей")
    void getAllUsersTest() {
        UserRole userRole = new UserRole();
        userRole.setId(1);
        userRole.setName("ROLE_USER");
        Collection<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        User user1 = new User(1L, "Иван", "Петров", "Сергеевич", 25,
                "ivan.petrov@example.com", "securePass123", userRoles);

        User user2 = new User(2L, "Анна", "Смирнова", "Игоревна", 30,
                "anna.smirnova@example.com", "annaPassword456", userRoles);

        Page<User> userPage = new PageImpl<>(List.of(user2, user1));

        UserDTO userDTO1 = new UserDTO("Иван", "Петров", "Сергеевич", 25);
        UserDTO userDTO2 = new UserDTO("Анна", "Смирнова", "Игоревна", 30);

        int pageNumber = 0;
        int pageSize = 10;

        when(userRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "userName"))))
                .thenReturn(userPage);

        when(userMapper.toUserDTO(user1)).thenReturn(userDTO1);
        when(userMapper.toUserDTO(user2)).thenReturn(userDTO2);

        Page<UserDTO> userPageDTO = userService.findAll(pageNumber, pageSize);

        assertThat(userPageDTO.getContent())
                .hasSize(2)
                .containsExactly(userDTO2, userDTO1);

        assertThat(userPageDTO.getContent())
                .extracting(UserDTO::getUserName)
                .containsExactly("Анна", "Иван");

        verify(userMapper, times(1)).toUserDTO(user1);
        verify(userMapper, times(1)).toUserDTO(user2);
    }

    @Test
    @DisplayName("Тест на создание пользователя - ошибка 404 Bad Request")
    void createUserNotFoundTest() {
        ResponseEntity<?> response = userService.createUser(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());

        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toUserDTO(any(User.class));
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    void createUserTest() {
        UserRole userRole = new UserRole();
        userRole.setId(1);
        userRole.setName("ROLE_USER");
        Collection<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        User user = new User(1L, "Иван", "Петров", "Сергеевич", 25,
                "ivan.petrov@example.com", "securePass123", userRoles);

        UserDTO userDTO = new UserDTO("Иван", "Петров", "Сергеевич", 25);

        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        ResponseEntity<?> response = userService.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());

        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toUserDTO(user);
    }

    @Test
    @DisplayName("Тест на обновление пользователя - ошибка 404 Bad Request")
    void updateUserNotFoundTest() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(userId, user));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(" User not found with id: " + userId, exception.getReason());

        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toUserDTO(any(User.class));
    }

    @Test
    @DisplayName("Успешное обновление пользователя")
    void updateUserTest() {
        Long userId = 1L;
        UserRole userRole = new UserRole();
        userRole.setId(1);
        userRole.setName("ROLE_USER");
        Collection<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        User existingUser = new User(1L, "Иван", "Петров", "Сергеевич", 25,
                "ivan.petrov@example.com", "securePass123", userRoles);

        User updatedUser = new User();

        updatedUser.setAge(26);

        User savedUser = new User(userId, existingUser.getUserName(), existingUser.getSurName(),
                existingUser.getPatronymic(), updatedUser.getAge(), existingUser.getEmail(), existingUser.getPassword(),
                existingUser.getRoles());

        UserDTO userDTO = new UserDTO(savedUser.getUserName(), savedUser.getSurName(), savedUser.getPatronymic(),
                savedUser.getAge());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toUserDTO(savedUser)).thenReturn(userDTO);

        ResponseEntity<?> response = userService.updateUser(userId, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());

        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
        verify(userMapper).updateFromUserDTO(updatedUser, existingUser);
        verify(userMapper).toUserDTO(savedUser);
    }

    @Test
    @DisplayName("Успешное удалении пользователя по id")
    void deleteUserTest() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Тест когда не найден id пользователя")
    void deleteUserNotFoundTest() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException runtimeException =  assertThrows(RuntimeException.class,
                () -> userService.deleteUser(userId));

        assertEquals("User not found with id: " + userId,  runtimeException.getMessage());

        verify(userRepository, never()).deleteById(userId);
        verify(userRepository, times(1)).findById(userId);

    }
}
