package com.thasya.inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockDTO {
    private Long id;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    private Integer reserved;

    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;

    private Integer availableQuantity;

    private Boolean outOfStock;

    private Boolean lowStock;

    private Long itemId;
}

