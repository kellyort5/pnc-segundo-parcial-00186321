package com.uca.pncsegundoparcialveterinaria.service.impl;

import com.uca.pncsegundoparcialveterinaria.common.ProductCategory;
import com.uca.pncsegundoparcialveterinaria.dto.request.ProductRequestDTO;
import com.uca.pncsegundoparcialveterinaria.dto.response.ProductResponseDTO;
import com.uca.pncsegundoparcialveterinaria.entities.Product;
import com.uca.pncsegundoparcialveterinaria.exceptions.BusinessRuleException;
import com.uca.pncsegundoparcialveterinaria.exceptions.ResourceNotFoundException;
import com.uca.pncsegundoparcialveterinaria.repository.ProductRepository;
import com.uca.pncsegundoparcialveterinaria.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO request) {

        if (productRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new BusinessRuleException(
                    "A product with this name already exists"
            );
        }

        Product product = buildProductFromRequest(request);

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts(
            ProductCategory category,
            Boolean available
    ) {

        List<Product> products;

        if (category != null && available != null) {

            products = productRepository
                    .findByCategoryAndAvailable(category, available);

        } else if (category != null) {

            products = productRepository.findByCategory(category);

        } else if (available != null) {

            products = productRepository.findByAvailable(available);

        } else {

            products = productRepository.findAll();
        }

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        )
                );

        return mapToResponse(product);
    }

    @Override
    public ProductResponseDTO updateProduct(
            Long id,
            ProductRequestDTO request
    ) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        )
                );

        productRepository.findByNameIgnoreCase(request.getName())
                .ifPresent(product -> {

                    if (!product.getId().equals(id)) {
                        throw new BusinessRuleException(
                                "Another product already uses this name"
                        );
                    }
                });

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setExpirationDate(request.getExpirationDate());
        existingProduct.setSupplier(request.getSupplier());

        existingProduct.setRequiresPrescription(
                request.getCategory() == ProductCategory.MEDICINE
                        || request.getCategory() == ProductCategory.VACCINE
        );

        if (request.getStock() == 0) {
            existingProduct.setAvailable(false);
        } else {
            existingProduct.setAvailable(
                    request.getAvailable()
            );
        }

        Product updatedProduct =
                productRepository.save(existingProduct);

        return mapToResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        )
                );

        if (
                product.getCategory() == ProductCategory.VACCINE
                        && product.getAvailable()
        ) {

            throw new BusinessRuleException(
                    "Available vaccines cannot be deleted"
            );
        }

        productRepository.delete(product);
    }

    private Product buildProductFromRequest(
            ProductRequestDTO request
    ) {

        Boolean requiresPrescription =
                request.getCategory() == ProductCategory.MEDICINE
                        || request.getCategory() == ProductCategory.VACCINE;

        Boolean available;

        if (request.getStock() == 0) {
            available = false;
        } else {
            available = request.getAvailable();
        }

        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .stock(request.getStock())
                .available(available)
                .requiresPrescription(requiresPrescription)
                .expirationDate(request.getExpirationDate())
                .supplier(request.getSupplier())
                .build();
    }

    private ProductResponseDTO mapToResponse(Product product) {

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .available(product.getAvailable())
                .requiresPrescription(product.getRequiresPrescription())
                .expirationDate(product.getExpirationDate())
                .supplier(product.getSupplier())
                .build();
    }
}