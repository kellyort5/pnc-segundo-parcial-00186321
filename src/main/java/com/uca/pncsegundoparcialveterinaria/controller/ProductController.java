package com.uca.pncsegundoparcialveterinaria.controller;

import com.uca.pncsegundoparcialveterinaria.common.ProductCategory;
import com.uca.pncsegundoparcialveterinaria.dto.request.ProductRequestDTO;
import com.uca.pncsegundoparcialveterinaria.dto.response.ProductResponseDTO;
import com.uca.pncsegundoparcialveterinaria.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(

            @RequestBody @Valid ProductRequestDTO request

    ) {

        return productService.createProduct(request);
    }

    @GetMapping
    public List<ProductResponseDTO> getAllProducts(

            @RequestParam(required = false)
            ProductCategory category,

            @RequestParam(required = false)
            Boolean available

    ) {

        return productService.getAllProducts(
                category,
                available
        );
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(
            @PathVariable Long id
    ) {

        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(

            @PathVariable Long id,

            @RequestBody @Valid
            ProductRequestDTO request

    ) {

        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);
    }
}