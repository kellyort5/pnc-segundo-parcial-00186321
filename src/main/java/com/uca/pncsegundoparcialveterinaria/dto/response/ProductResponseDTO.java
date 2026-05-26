package com.uca.pncsegundoparcialveterinaria.dto.response;

import com.uca.pncsegundoparcialveterinaria.common.ProductCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private ProductCategory category;
    private BigDecimal price;
    private Integer stock;
    private Boolean available;
    private Boolean requiresPrescription;
    private LocalDate expirationDate;
    private String supplier;
}
