package com.thasya.inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemVariantDTO {
    private Long id;

    @NotBlank(message = "Variant name is required")
    private String name;

    private String color;

    private String size;

    private String description;

    @NotNull(message = "Variant price is required")
    private BigDecimal price;

    private Long itemId;

    private VariantStockDTO variantStock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

