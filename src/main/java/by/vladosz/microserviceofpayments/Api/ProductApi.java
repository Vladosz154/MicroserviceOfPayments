package by.vladosz.microserviceofpayments.Api;

import by.vladosz.microserviceofpayments.dto.ProductDTO;
import by.vladosz.microserviceofpayments.models.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Product API",
        description = "API для управления продуктами. Поддерживает CRUD операции, поиск и сортировку с пагинацией.")
public interface ProductApi {

    @Operation(
            summary = "Создать новый продукт",
            description = "Сохраняет новый продукт в систему после проверки валидации."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Продукт успешно создан",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    ResponseEntity<?> save(@RequestBody @Valid Product product, BindingResult bindingResult);

    @Operation(
            summary = "Получить список продуктов с пагинацией",
            description = """
                    Возвращает страницу (page) продуктов с возможностью настройки:
                            - Номера страницы (page)
                            - Количества элементов на странице (size)
                    
                            По умолчанию:
                            - page = 0 (первая страница)
                            - size = 10 (10 элементов на странице)
                    
                            В ответе содержится:
                            - Список продуктов в формате DTO
                            - Информация о пагинации (общее количество элементов, страниц и т.д.)
                    """
    )
    Page<ProductDTO> findAll(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                             @RequestParam(name = "size", defaultValue = "10") int pageSize);

    @Operation(
            summary = "Поиск продуктов по названию с пагинацией",
            description = """
                    Возвращает страницу продуктов, отфильтрованных по названию.
                    Поддерживает пагинацию через стандартные параметры page и size.
                    
                    Пример использования:
                    - /api/products/name/Телефон?page=0&size=5
                    """
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Продукты по названию успешно найдены",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ошибка валидации входных данных"
            )
    })
    Page<ProductDTO> findByNameProduct(@PathVariable String nameProduct,
                                       @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                       @RequestParam(name = "size", defaultValue = "10") int pageSize);

    @Operation(
            summary = "Поиск продуктов по категории с пагинацией",
            description = """
                    Возвращает страницу продуктов, отфильтрованных по категории.
                    Поддерживает стандартные параметры пагинации Spring Data.
                    
                    Примеры использования:
                    - /api/products/category/Электроника?page=0&size=5
                    - /api/products/category/Одежда?size=20
                    """
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Продукты по категории успешно найдены",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных"
            )
    })
    Page<ProductDTO> findByCategoryProduct(@PathVariable String categoryProduct,
                                           @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                           @RequestParam(name = "size", defaultValue = "10") int pageSize);

    @Operation(
            summary = "Обновить существующий продукт",
            description = """
                    Обновляет данные продукта по его идентификатору.
                    Проводит валидацию входных данных перед обновлением.
                    
                    Требования:
                    - ID продукта должен существовать в системе
                    - Тело запроса должно содержать все обязательные поля продукта
                    """
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Продукт успешно обновлен",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продукт не найден"
            ),

    })
    ResponseEntity<?> update(@PathVariable Long product_id, @RequestBody @Valid
    Product product, BindingResult bindingResult);

    @Operation(
            summary = "Удалить существующий продукт",
            description = """
                    Удаляем данные продукта по его идентификатору.
                    
                    Требования:
                    - ID продукта должен существовать в системе
                    """
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукт удален"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    void delete(@PathVariable Long product_id);

    @Operation(
            summary = "Получить продукты с сортировкой по названию",
            description = """
                    Возвращает страницу продуктов, отсортированных по названию в алфавитном порядке (A-Z).
                    Поддерживает стандартные параметры пагинации.
                    
                    Особенности:
                    - Сортировка выполняется на сервере
                    - Регистр символов не учитывается (case-insensitive)
                    - Пустые значения названий идут последними
                    
                    Пример использования:
                    - /api/products/sortByName?page=0&size=5
                    """
    )

    @ApiResponse(responseCode = "200", description = "Успешный запрос. Возвращает отсортированную страницу продуктов по имени.")
    Page<ProductDTO> sortByName(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                @RequestParam(name = "size", defaultValue = "10") int pageSize);

    @Operation(
            summary = "Получить продукты с сортировкой по названию",
            description = """
                    Возвращает страницу продуктов, отсортированных по названию в обратном алфавитном порядке (Я-A).
                    Поддерживает стандартные параметры пагинации.
                    
                    Особенности:
                    - Сортировка выполняется на сервере
                    - Регистр символов не учитывается (case-insensitive)
                    - Пустые значения названий идут последними
                    
                    Пример использования:
                    - /api/products/sortByNameDescending?page=0&size=5
                    """
    )

    @ApiResponse(responseCode = "200", description = "Успешный запрос. Возвращает отсортированную страницу продуктов по имени в обратном порядке.")
    Page<ProductDTO> sortByNameDescending(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                          @RequestParam(name = "size", defaultValue = "10") int pageSize);

    @Operation(
            summary = "Получить продукты с сортировкой по цене",
            description = """
                    Возвращает страницу продуктов, отсортированных по возрастанию цены.
                    Поддерживает стандартные параметры пагинации.
                    
                    Особенности:
                    - Сортировка выполняется на сервере
                    
                    Пример использования:
                    - /api/products/sortByPrice?page=0&size=5
                    """
    )

    @ApiResponse(responseCode = "200", description = "Успешный запрос. Возвращает отсортированную страницу продуктов по цене.")
    Page<ProductDTO> sortByPrice(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                 @RequestParam(name = "size", defaultValue = "10") int pageSize);

    @Operation(
            summary = "Получить продукты с сортировкой по цене",
            description = """
                    Возвращает страницу продуктов, отсортированных по убыванию цены.
                    Поддерживает стандартные параметры пагинации.
                    
                    Особенности:
                    - Сортировка выполняется на сервере
                    
                    Пример использования:
                    - /api/products/sortByPriceDescending?page=0&size=5
                    """
    )

    @ApiResponse(responseCode = "200", description = "Успешный запрос. Возвращает отсортированную страницу продуктов в обратном порядке по цене.")
    Page<ProductDTO> sortByPriceDescending(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                           @RequestParam(name = "size", defaultValue = "10") int pageSize);


}
