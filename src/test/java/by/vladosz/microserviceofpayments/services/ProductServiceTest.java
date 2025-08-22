package by.vladosz.microserviceofpayments.services;

import by.vladosz.microserviceofpayments.dto.ProductDTO;
import by.vladosz.microserviceofpayments.mappers.ProductMapper;
import by.vladosz.microserviceofpayments.models.CategoryProduct;
import by.vladosz.microserviceofpayments.models.Product;
import by.vladosz.microserviceofpayments.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;


    @Test
    public void findAllProducts() {
        int pageNumber = 0;
        int pageSize = 10;

        Product product1 = new Product(1L, "Микроволновка LG", "Компактная микроволновая печь с грилем", 8.990, "RUB", 25, CategoryProduct.БЫТОВАЯ_ТЕХНИКА);
        Product product2 = new Product(2L, "Джинсы Levi's 501", "Классические джинсы прямого кроя", 5.999, "RUB", 75, CategoryProduct.ОДЕЖДА);

        Page<Product> productPage = new PageImpl<>(List.of(product1, product2));

        ProductDTO productDTO1 = new ProductDTO("Микроволновка LG", "Компактная микроволновая печь с грилем", 8.990, CategoryProduct.БЫТОВАЯ_ТЕХНИКА);
        ProductDTO productDTO2 = new ProductDTO("Джинсы Levi's 501", "Классические джинсы прямого кроя", 5.999, CategoryProduct.ОДЕЖДА);

        when(productRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "name")))).thenReturn(productPage);

        when(productMapper.toProductDTO(product1)).thenReturn(productDTO1);
        when(productMapper.toProductDTO(product2)).thenReturn(productDTO2);

        Page<ProductDTO> result = productService.findAll(pageNumber, pageSize);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).containsExactly(productDTO1, productDTO2);
        assertThat(result.getContent()).extracting(ProductDTO::getName)
                .containsExactly("Микроволновка LG", "Джинсы Levi's 501");

        verify(productMapper).toProductDTO(product1);
        verify(productMapper).toProductDTO(product2);
    }


    @Test
    @DisplayName("Тест где есть по id продукт")
    void findById() {
        Product product2 = new Product(2L, "Джинсы Levi's 501", "Классические джинсы прямого кроя", 5.999, "RUB", 75, CategoryProduct.ОДЕЖДА);

        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        String name = productService.getProductName(2L);

        assertEquals("Джинсы Levi's 501", name);
        verify(productRepository).findById(2L);

    }

    @Test
    @DisplayName("Тест где нет по id продукта")
    void findByIdNotFound() {

        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getProductName(3L));

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository).findById(3L);

    }

    @Test
    @DisplayName("Тест когда продукт найден")
    void findByProduct() {
        Product product2 = new Product(2L, "Джинсы Levi's 501", "Классические джинсы прямого кроя", 5999.0, "RUB", 75, CategoryProduct.ОДЕЖДА);

        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        String name = productService.getProductName(2L);

        assertEquals("Джинсы Levi's 501", name);

        verify(productRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Тест когда продукт не найден")
    void findNotProduct() {

        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getProductName(3L));

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Тест на успешное удаление продукта по id")
    void deleteProductSuccessfullyTest() {
        Product product = new Product();
        Long productId = 2L;
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    @DisplayName("Тест на удаление продукта по id - продукт не найден")
    void deleteProductWhenProductNotFoundTest() {
        Long productId = 2L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.deleteProduct(productId));

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Успешное создание товара")
    void createProduct_Success() {
        // Подготовка данных
        Product product = new Product(1L, "Микроволновка LG", "Компактная микроволновая печь с грилем", 8.990, "RUB", 25, CategoryProduct.БЫТОВАЯ_ТЕХНИКА);

        ProductDTO productDTO = new ProductDTO("Микроволновка LG", "Компактная микроволновая печь с грилем", 8.990, CategoryProduct.БЫТОВАЯ_ТЕХНИКА);

        when(productRepository.save(eq(product))).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(productDTO);

        ResponseEntity<?> response = productService.createProduct(product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());

        verify(productRepository).save(product);
        verify(productMapper).toProductDTO(product);
    }

    @Test
    @DisplayName("Тест на создание продукта - ошибка 404 Bad Request")
    void addProductTestFound() {
        ResponseEntity<?> response = productService.createProduct(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Product is null", response.getBody());

        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toProductDTO(any());

    }

    @Test
    @DisplayName("Успешное обновление товара")
    void updatedProduct_Success() {
        Long productId = 1L;

        Product existingProduct = new Product(productId, "Микроволновка LG",
                "Компактная микроволновая печь с грилем", 8990.0, "RUB",
                25, CategoryProduct.БЫТОВАЯ_ТЕХНИКА);

        Product productUpdate = new Product();
        productUpdate.setName("Микроволновка LG New Model");
        productUpdate.setDescription("Новая модель с улучшенными характеристиками");
        productUpdate.setPrice(9.990);

        Product savedProduct = new Product(productId, productUpdate.getName(), productUpdate.getDescription(),
                productUpdate.getPrice(), "RUB", 25, CategoryProduct.БЫТОВАЯ_ТЕХНИКА);

        ProductDTO productDTO = new ProductDTO(savedProduct.getName(), savedProduct.getDescription(),
                savedProduct.getPrice(), savedProduct.getCategory());

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toProductDTO(savedProduct)).thenReturn(productDTO);


        ResponseEntity<?> response = productService.updateProduct(productId, productUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());

        verify(productRepository).findById(productId);
        verify(productMapper).updateFromProductDTO(productUpdate, existingProduct);
        verify(productRepository).save(existingProduct);
        verify(productMapper).toProductDTO(savedProduct);
    }


    @Test
    @DisplayName("Тест на обновление продукта по id - продукт не найден")
    void updateProductWhenProductNotFoundTest() {
        Product product = new Product();
        Long productId = 2L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> productService.updateProduct(productId, product));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Product not found with id: " + productId, exception.getReason());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("тест на сортировку товаров по цене (по возрастанию)")
    void sortByPriceTest() {
        int pageNumber = 0;
        int pageSize = 10;

        Product product1 = new Product(1L, "Микроволновка LG", "Компактная микроволновая печь с грилем", 8990., "RUB", 25, CategoryProduct.БЫТОВАЯ_ТЕХНИКА);
        Product product2 = new Product(2L, "Джинсы Levi's 501", "Классические джинсы прямого кроя", 5999., "RUB", 75, CategoryProduct.ОДЕЖДА);

        Page<Product> productPage = new PageImpl<>(List.of(product2, product1));

        ProductDTO productDTO1 = new ProductDTO("Микроволновка LG", "Компактная микроволновая печь с грилем", 8990., CategoryProduct.БЫТОВАЯ_ТЕХНИКА);
        ProductDTO productDTO2 = new ProductDTO("Джинсы Levi's 501", "Классические джинсы прямого кроя", 5999., CategoryProduct.ОДЕЖДА);

        when(productRepository.findAllByOrderByPriceAsc(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "price")))).thenReturn(productPage);

        when(productMapper.toProductDTO(product1)).thenReturn(productDTO1);
        when(productMapper.toProductDTO(product2)).thenReturn(productDTO2);

        Page<ProductDTO> result = productService.sortByPrice(pageNumber, pageSize);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).containsExactly(productDTO2, productDTO1);
        assertThat(result.getContent()).extracting(ProductDTO::getPrice)
                .containsExactly(5999., 8990.);

        verify(productMapper).toProductDTO(product1);
        verify(productMapper).toProductDTO(product2);

    }

    @Test
    @DisplayName("Проверка теста на категории которых не существует")
    void getProductsCategoryTest() {
        int pageNumber = 0;
        int pageSize = 10;
        String category = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productService.getProductCategory(category, pageNumber, pageSize));

        assertEquals("Product category cannot be null", exception.getMessage());
        verify(productRepository, never()).findAll(any(PageRequest.class));
        verify(productRepository, never()).findByCategory(any(Pageable.class), any());
    }

    @Test
    @DisplayName("Проверка теста на категории которые существуют")
    void getProductsByCategoryTest() {
        int pageNumber = 0;
        int pageSize = 10;
        String category = String.valueOf(CategoryProduct.ЭЛЕКТРОНИКА);
        Product product1 = new Product(1L, "Наушники Sony", "Беспроводные наушники", 8990d,
                "RUB", 25, CategoryProduct.ЭЛЕКТРОНИКА);
        Product product2 = new Product(2L, "Смартфон Xiaomi", "Флагманский смартфон", 59999d,
                "RUB", 75, CategoryProduct.ЭЛЕКТРОНИКА);

        List<Product> products = List.of(product1, product2);
        Page<Product> productPage = new PageImpl<>(products);

        ProductDTO productDTO1 = new ProductDTO("Наушники Sony", "Беспроводные наушники",
                8990d, CategoryProduct.ЭЛЕКТРОНИКА);
        ProductDTO productDTO2 = new ProductDTO("Смартфон Xiaomi", "Флагманский смартфон",
                59999d, CategoryProduct.ЭЛЕКТРОНИКА);

        when(productRepository.findByCategory(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "price")),
                CategoryProduct.ЭЛЕКТРОНИКА)).thenReturn(productPage);

        when(productMapper.toProductDTO(product1)).thenReturn(productDTO1);
        when(productMapper.toProductDTO(product2)).thenReturn(productDTO2);

        Page<ProductDTO> page = productService.getProductCategory(category, pageNumber, pageSize);

        verify(productRepository, times(1))
                .findByCategory(any(PageRequest.class), eq(CategoryProduct.ЭЛЕКТРОНИКА));

        List<ProductDTO> content = page.getContent();
        assertEquals(2, content.size());

        assertEquals(8990d, content.get(0).getPrice());
        assertEquals(59999d, content.get(1).getPrice());

        assertEquals("Наушники Sony", content.get(0).getName());
        assertEquals("Смартфон Xiaomi", content.get(1).getName());
    }


}
