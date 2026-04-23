package com.thasya.inventory.controller;

import com.thasya.inventory.dto.VariantStockDTO;
import com.thasya.inventory.service.VariantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API Controller for Variant Stock Management
 * Base path: /api/v1/variant-stocks
 */
@RestController
@RequestMapping("/api/v1/variant-stocks")
@RequiredArgsConstructor
public class VariantStockController {

    private final VariantStockService variantStockService;

    /**
     * Get all variant stocks
     * GET /api/v1/variant-stocks
     */
    @GetMapping
    public ResponseEntity<List<VariantStockDTO>> getAllVariantStocks() {
        List<VariantStockDTO> stocks = variantStockService.getAllVariantStocks();
        return ResponseEntity.ok(stocks);
    }

    /**
     * Get variant stock by ID
     * GET /api/v1/variant-stocks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VariantStockDTO> getVariantStockById(@PathVariable Long id) {
        VariantStockDTO stock = variantStockService.getVariantStockById(id);
        return ResponseEntity.ok(stock);
    }

    /**
     * Get stock for a variant
     * GET /api/v1/variant-stocks/variant/{variantId}
     */
    @GetMapping("/variant/{variantId}")
    public ResponseEntity<VariantStockDTO> getStockByVariantId(@PathVariable Long variantId) {
        VariantStockDTO stock = variantStockService.getStockByVariantId(variantId);
        return ResponseEntity.ok(stock);
    }

    /**
     * Update variant stock
     * PUT /api/v1/variant-stocks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<VariantStockDTO> updateVariantStock(@PathVariable Long id,
                                                              @Valid @RequestBody VariantStockDTO stockDTO) {
        VariantStockDTO updated = variantStockService.updateVariantStock(id, stockDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Get all low stock variants
     * GET /api/v1/variant-stocks/alerts/low-stock
     */
    @GetMapping("/alerts/low-stock")
    public ResponseEntity<List<VariantStockDTO>> getLowStockVariants() {
        List<VariantStockDTO> lowStockVariants = variantStockService.getLowStockVariants();
        return ResponseEntity.ok(lowStockVariants);
    }

    /**
     * Get all out of stock variants
     * GET /api/v1/variant-stocks/alerts/out-of-stock
     */
    @GetMapping("/alerts/out-of-stock")
    public ResponseEntity<List<VariantStockDTO>> getOutOfStockVariants() {
        List<VariantStockDTO> outOfStockVariants = variantStockService.getOutOfStockVariants();
        return ResponseEntity.ok(outOfStockVariants);
    }
}

