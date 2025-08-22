package by.vladosz.microserviceofpayments.controllers;

import by.vladosz.microserviceofpayments.dto.ProductDTO;
import by.vladosz.microserviceofpayments.models.CategoryProduct;

import by.vladosz.microserviceofpayments.services.ProductService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
public class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;


    @Test
    void findAllTest() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;

        ProductDTO testProduct = new ProductDTO(
                "Test Product",
                "Test Description",
                100.0,
                CategoryProduct.ЭЛЕКТРОНИКА
        );

        Page<ProductDTO> mockPage = new PageImpl<>(List.of(testProduct));

        when(productService.findAll(pageNumber, pageSize)).thenReturn(mockPage);

        mockMvc.perform(get("/api/products")
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andExpect(status().isOk());
    }


    @Test
    void findByNameProduct() throws Exception {
        String nameProduct = "Телефон";
        int pageNumber = 0;
        int pageSize = 10;

        ProductDTO testProduct = new ProductDTO(
                nameProduct,
                "Test Description",
                100.0,
                CategoryProduct.ЭЛЕКТРОНИКА
        );

        when(productService.getProductName(nameProduct, pageNumber, pageSize))
                .thenReturn(new PageImpl<>(List.of(testProduct)));

        mockMvc.perform(
                        get("/api/products/name/{nameProduct}", nameProduct)
                                .param("page", String.valueOf(pageNumber))
                                .param("size", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void deleteProductTest() throws Exception {
        Long productId = 2L;

        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(
                delete("/api/products/{productId}", productId))
                .andExpect(status().isOk());

        verify(productService).deleteProduct(productId);
    }


}
