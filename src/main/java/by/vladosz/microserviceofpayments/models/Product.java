package by.vladosz.microserviceofpayments.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotEmpty(message = "Название продукта не может быть пустым")
    @Size(min = 2, max = 50, message = "Название продукта должно быть от 2 до 50 символов")
    private String name;

    @NotEmpty(message = "Описание продукта не может быть пустым")
    @Size(min = 10, max = 200, message = "Описание продукта должно быть от 10 до 200 символов")
    private String description;

    @Min(value = 1, message = "Цена продукта должна быть больше 0")
    private Double price;

    @NotEmpty(message = "Валюта продукта не может быть пустым")
    private String currency;

    @DecimalMin(value = "0.01", message = "Количество продукта должна быть больше 0")
    private int count;

    @NotNull(message = "Категория продукта не может быть пустым")
    @Enumerated(EnumType.STRING)
    private CategoryProduct category;
}
