package by.vladosz.microserviceofpayments.repositories;

import by.vladosz.microserviceofpayments.dto.UserDTO;
import by.vladosz.microserviceofpayments.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String name);
}
