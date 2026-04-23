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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDTO {
    private Long id;

    @NotBlank(message = "Item name is required")
    private String name;

    @NotBlank(message = "Item SKU is required")
    private String sku;

    private String description;

    @NotNull(message = "Base price is required")
    private BigDecimal basePrice;

    private StockDTO stock;

    private List<ItemVariantDTO> variants;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

