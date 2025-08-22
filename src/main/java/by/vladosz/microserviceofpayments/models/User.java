package by.vladosz.microserviceofpayments.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Getter
@Setter
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 20, message = "Имя пользователя должно быть от 2 до 20 символов")
    private String userName;

    @NotEmpty(message = "Фамилия пользователя не может быть пустым")
    @Size(min = 2, max = 30, message = "Фамилия пользователя должно быть от 2 до 30 символов")
    private String surName;

    @NotEmpty(message = "Отчество пользователя не может быть пустым")
    @Size(min = 3, max = 30, message = "Отчество пользователя должно быть от 3 до 30 символов")
    private String patronymic;

    @Min(value = 18, message = "Возраст не может быть меньше 18")
    @Max(value = 120, message = "Возраст не может быть больше 120")
    private int age;

    @NotEmpty(message = "Email пользователя не может быть пустым")
    @Size(min = 5, max = 30, message = "Email пользователя должно быть от 5 до 20 символов")
    private String email;

    @NotEmpty(message = "Пароль пользователя не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль пользователя должно быть от 8 до 30 символов")
    private String password;

    @NotNull(message = "Роль пользователя не может быть пустым")
    @Enumerated(EnumType.STRING)
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<UserRole> roles;





}
