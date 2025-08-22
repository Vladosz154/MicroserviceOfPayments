package by.vladosz.microserviceofpayments.controllers;

import by.vladosz.microserviceofpayments.Api.UserApi;
import by.vladosz.microserviceofpayments.dto.UserDTO;
import by.vladosz.microserviceofpayments.models.User;
import by.vladosz.microserviceofpayments.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    @GetMapping
    public Page<UserDTO> findAllUsers(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                      @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        return userService.findAll(pageNumber, pageSize);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return userService.createUser(user);
    }

    @PutMapping("{user_id}")
    public ResponseEntity<?> updateUser(@PathVariable Long user_id, @RequestBody @Valid
    User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return userService.updateUser(user_id, user);
    }

    @Override
    @GetMapping("{user_id}")
    public UserDTO findUserById(@PathVariable Long user_id) {
        return userService.findUserById(user_id);
    }

    @Override
    @DeleteMapping("{user_id}")
    public void deleteUser(@PathVariable Long user_id) {
        userService.deleteUser(user_id);
    }

//    @GetMapping("/csrf-token")
//    public CsrfToken getCsrfToken(HttpServletRequest request) {
//        return (CsrfToken) request.getAttribute("_csrf");
//    }

}
