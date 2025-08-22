package by.vladosz.microserviceofpayments.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "userRoles")
@Data
public class UserRole {
    @Id
    private Integer id;

    private String name;

}
