package by.vladosz.microserviceofpayments.services;

import by.vladosz.microserviceofpayments.dto.ProductDTO;
import by.vladosz.microserviceofpayments.mappers.ProductMapper;
import by.vladosz.microserviceofpayments.models.CategoryProduct;
import by.vladosz.microserviceofpayments.models.Product;
import by.vladosz.microserviceofpayments.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    public Page<ProductDTO> findAll(int pageNumber, int pageSize) {

        return productRepository.findAll(PageRequest.of(pageNumber, pageSize,
                        Sort.by(Sort.Direction.ASC, "name")))
                .map(productMapper::toProductDTO);
    }

    public ResponseEntity<?> createProduct(Product product) {
        if (product == null) {
            return ResponseEntity.badRequest().body("Product is null");
        }
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(productMapper.toProductDTO(savedProduct));
    }

    public Page<ProductDTO> getProductName(String name, int pageNumber, int pageSize) {
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        return productRepository.findByName(PageRequest.of(pageNumber, pageSize,
                        Sort.by(Sort.Direction.ASC, "price")), name)
                .map(productMapper::toProductDTO);
    }

    public Page<ProductDTO> getProductCategory(String category, int pageNumber, int pageSize) {
        if (category == null) {
            throw new IllegalArgumentException("Product category cannot be null");
        }
        return productRepository.findByCategory(PageRequest.of(pageNumber, pageSize,
                        Sort.by(Sort.Direction.ASC, "price")), CategoryProduct.valueOf(category))
                .map(productMapper::toProductDTO);
    }

    public ResponseEntity<?> updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id));

        productMapper.updateFromProductDTO(product, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);
        return ResponseEntity.ok().body(productMapper.toProductDTO(updatedProduct));
    }

    public void deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.deleteById(id);
    }

    public Page<ProductDTO> sortByName(int pageNumber, int pageSize) {
        return productRepository.findAllByOrderByNameAsc(PageRequest.of(pageNumber, pageSize,
                        Sort.by(Sort.Direction.ASC, "name")))
                .map(productMapper::toProductDTO);
    }

    public Page<ProductDTO> sortByNameDescending(int pageNumber, int pageSize) {
        return productRepository.findAllByOrderByNameDesc(PageRequest.of(pageNumber, pageSize,
                        Sort.by(Sort.Direction.DESC, "name")))
                .map(productMapper::toProductDTO);
    }

    public Page<ProductDTO> sortByPrice(int pageNumber, int pageSize) {
        return productRepository.findAllByOrderByPriceAsc(PageRequest.of(pageNumber, pageSize,
                        Sort.by(Sort.Direction.ASC, "price")))
                .map(productMapper::toProductDTO);
    }

    public Page<ProductDTO> sortByPriceDescending(int pageNumber, int pageSize) {
        return productRepository.findAllByOrderByPriceDesc(PageRequest.of(pageNumber, pageSize,
                        Sort.by(Sort.Direction.DESC, "price")))
                .map(productMapper::toProductDTO);
    }

    public String getProductName(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Product not found")).getName();
    }

}
