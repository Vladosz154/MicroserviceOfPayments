package by.vladosz.microserviceofpayments.mappers;

import by.vladosz.microserviceofpayments.dto.ProductDTO;
import by.vladosz.microserviceofpayments.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "id", ignore = true)
    void updateFromProductDTO(Product productDTO, @MappingTarget Product product);

}
