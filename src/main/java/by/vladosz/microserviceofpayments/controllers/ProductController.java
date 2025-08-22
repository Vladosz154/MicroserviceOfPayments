package by.vladosz.microserviceofpayments.controllers;

import by.vladosz.microserviceofpayments.Api.ProductApi;
import by.vladosz.microserviceofpayments.dto.ProductDTO;
import by.vladosz.microserviceofpayments.models.Product;
import by.vladosz.microserviceofpayments.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @GetMapping
    public Page<ProductDTO> findAll(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                    @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        return productService.findAll(pageNumber, pageSize);
    }

    @Override
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return productService.createProduct(product);
    }

    @Override
    @GetMapping("/name/{nameProduct}")
    public Page<ProductDTO> findByNameProduct(@PathVariable String nameProduct,
                                              @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                              @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        return productService.getProductName(nameProduct, pageNumber, pageSize);
    }

    @Override
    @GetMapping("/category/{categoryProduct}")
    public Page<ProductDTO> findByCategoryProduct(@PathVariable String categoryProduct,
                                                  @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                  @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        return productService.getProductCategory(categoryProduct, pageNumber, pageSize);
    }

    @Override
    @PutMapping("{product_id}")
    public ResponseEntity<?> update(@PathVariable Long product_id, @RequestBody @Valid
    Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return productService.updateProduct(product_id, product);
    }

    @Override
    @DeleteMapping("{product_id}")
    public void delete(@PathVariable Long product_id) {
        productService.deleteProduct(product_id);
    }

    @Override
    @GetMapping("/sortByName")
    public Page<ProductDTO> sortByName(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                       @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        return productService.sortByName(pageNumber, pageSize);
    }

    @Override
    @GetMapping("/sortByNameDescending")
    public Page<ProductDTO> sortByNameDescending(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                 @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        return productService.sortByNameDescending(pageNumber, pageSize);
    }

    @Override
    @GetMapping("/sortByPrice")
    public Page<ProductDTO> sortByPrice(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                        @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        return productService.sortByPrice(pageNumber, pageSize);
    }

    @Override
    @GetMapping("/sortByPriceDescending")
    public Page<ProductDTO> sortByPriceDescending(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                  @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        return productService.sortByPriceDescending(pageNumber, pageSize);
    }
}
