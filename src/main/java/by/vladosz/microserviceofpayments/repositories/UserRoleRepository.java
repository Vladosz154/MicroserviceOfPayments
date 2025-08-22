package by.vladosz.microserviceofpayments.repositories;

import by.vladosz.microserviceofpayments.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findByName(String username);
}
