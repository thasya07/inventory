package com.thasya.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCreationRequest {

    @Valid
    @NotNull(message = "Item details are required")
    private ItemDTO item;

    @Valid
    private StockDTO stock;
}

