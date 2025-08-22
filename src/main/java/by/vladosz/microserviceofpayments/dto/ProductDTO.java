package by.vladosz.microserviceofpayments.dto;

import by.vladosz.microserviceofpayments.models.CategoryProduct;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @NotEmpty(message = "Название продукта не может быть пустым")
    @Size(min = 2, max = 50, message = "Название продукта должно быть от 2 до 50 символов")
    private String name;

    @NotEmpty(message = "Описание продукта не может быть пустым")
    @Size(min = 10, max = 200, message = "Описание продукта должно быть от 10 до 200 символов")
    private String description;

    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    private Double price;

    @NotNull(message = "Категория продукта не может быть пустой")
    private CategoryProduct category;

}



