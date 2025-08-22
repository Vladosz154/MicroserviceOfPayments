package by.vladosz.microserviceofpayments.Api;

import by.vladosz.microserviceofpayments.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {

    @Operation(
            summary = "Получить список пользователей с пагинацией",
            description = """
                    Возвращает страницу (page) пользователей с возможностью настройки:
                            - Номера страницы (page)
                            - Количества пользователей на странице (size)
                    
                            По умолчанию:
                            - page = 0 (первая страница)
                            - size = 10 (10 элементов на странице)
                    
                            В ответе содержится:
                            - Список продуктов в формате DTO
                            - Информация о пагинации (общее количество пользователей, страниц и т.д.)
                    """
    )
    Page<UserDTO> findAllUsers(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                          @RequestParam(name = "size", defaultValue = "10") int pageSize);

    @Operation(
            summary = "Поиск пользователя по id",
            description = """
                    Пример использования:
                    - /api/users/1
                    """
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь по id успешно найден",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ошибка пользователь с указаным id не найден"
            )
    })

    UserDTO findUserById(@PathVariable Long user_id);

    @Operation(
            summary = "Удаление пользователя по id",
            description = """

                    Пример использования:
                    - /api/users/delete/1
                    """
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь по id успешно удален",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ошибка пользователь с указаным id не найден"
            )
    })

    void deleteUser(@PathVariable Long user_id);
}
