package by.vladosz.microserviceofpayments.mappers;

import by.vladosz.microserviceofpayments.dto.UserDTO;
import by.vladosz.microserviceofpayments.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    void updateFromUserDTO(User userDTO, @MappingTarget User user);
}
