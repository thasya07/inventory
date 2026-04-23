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
public class VariantCreationRequest {

    @Valid
    @NotNull(message = "Variant details are required")
    private ItemVariantDTO variant;

    @Valid
    private VariantStockDTO stock;
}

