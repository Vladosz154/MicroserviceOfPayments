package by.vladosz.microserviceofpayments.repositories;

import by.vladosz.microserviceofpayments.models.CategoryProduct;
import by.vladosz.microserviceofpayments.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByName(Pageable pageable, String name);

    Page<Product> findByCategory(Pageable pageable, CategoryProduct category);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByOrderByNameAsc(Pageable pageable);

    Page<Product> findAllByOrderByNameDesc(Pageable pageable);

    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);

    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);

    Optional<Product> findById(Long id);
}
