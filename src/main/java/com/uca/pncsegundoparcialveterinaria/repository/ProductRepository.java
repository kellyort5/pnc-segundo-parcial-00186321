package com.uca.pncsegundoparcialveterinaria.repository;

import com.uca.pncsegundoparcialveterinaria.common.ProductCategory;
import com.uca.pncsegundoparcialveterinaria.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByNameIgnoreCase(String name);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByAvailable(Boolean available);

    List<Product> findByCategoryAndAvailable(
            ProductCategory category,
            Boolean available
    );
}

