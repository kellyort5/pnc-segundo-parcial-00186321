package com.uca.pncsegundoparcialveterinaria.service;


import com.uca.pncsegundoparcialveterinaria.common.ProductCategory;
import com.uca.pncsegundoparcialveterinaria.dto.request.ProductRequestDTO;
import com.uca.pncsegundoparcialveterinaria.dto.response.ProductResponseDTO;
import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO request);

    List<ProductResponseDTO> getAllProducts(
            ProductCategory category,
            Boolean available
    );

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO updateProduct(
            Long id,
            ProductRequestDTO request
    );

    void deleteProduct(Long id);
}
