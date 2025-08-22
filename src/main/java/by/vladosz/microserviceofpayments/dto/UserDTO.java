package by.vladosz.microserviceofpayments.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    @NotEmpty(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 20, message = "Имя пользователя должно быть от 2 до 20 символов")
    private String userName;

    @NotEmpty(message = "Фамилия пользователя не может быть пустым")
    @Size(min = 2, max = 30, message = "Фамилия пользователя должно быть от 2 до 30 символов")
    private String surName;

    @NotEmpty(message = "Отчество пользователя не может быть пустым")
    @Size(min = 3, max = 30, message = "Отчество пользователя должно быть от 3 до 30 символов")
    private String patronymic;

    @NotEmpty(message = "Отчество пользователя не может быть пустым")
    @Min(value = 18, message = "Возраст не может быть меньше 18")
    @Max(value = 120, message = "Возраст не может быть больше 120")
    private int age;

}
