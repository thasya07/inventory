package com.thasya.inventory.controller;

import com.thasya.inventory.dto.*;
import com.thasya.inventory.service.ItemVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API Controller for Item Variant Management
 * Base path: /api/v1/items/{itemId}/variants
 */
@RestController
@RequestMapping("/api/v1/items/{itemId}/variants")
@RequiredArgsConstructor
public class ItemVariantController {

    private final ItemVariantService itemVariantService;

    /**
     * Create a new variant for an item
     * POST /api/v1/items/{itemId}/variants
     */
    @PostMapping
    public ResponseEntity<ItemVariantDTO> createVariant(@PathVariable Long itemId,
                                                        @Valid @RequestBody VariantCreationRequest request) {
        ItemVariantDTO created = itemVariantService.createVariant(itemId, request.getVariant(), request.getStock());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get all variants for an item
     * GET /api/v1/items/{itemId}/variants
     */
    @GetMapping
    public ResponseEntity<List<ItemVariantDTO>> getVariantsByItemId(@PathVariable Long itemId) {
        List<ItemVariantDTO> variants = itemVariantService.getVariantsByItemId(itemId);
        return ResponseEntity.ok(variants);
    }

    /**
     * Get variant by ID
     * GET /api/v1/items/{itemId}/variants/{variantId}
     */
    @GetMapping("/{variantId}")
    public ResponseEntity<ItemVariantDTO> getVariantById(@PathVariable Long itemId, @PathVariable Long variantId) {
        ItemVariantDTO variant = itemVariantService.getVariantById(variantId);
        return ResponseEntity.ok(variant);
    }

    /**
     * Update variant
     * PUT /api/v1/items/{itemId}/variants/{variantId}
     */
    @PutMapping("/{variantId}")
    public ResponseEntity<ItemVariantDTO> updateVariant(@PathVariable Long itemId,
                                                        @PathVariable Long variantId,
                                                        @Valid @RequestBody ItemVariantDTO variantDTO) {
        ItemVariantDTO updated = itemVariantService.updateVariant(variantId, variantDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete variant
     * DELETE /api/v1/items/{itemId}/variants/{variantId}
     */
    @DeleteMapping("/{variantId}")
    public ResponseEntity<Void> deleteVariant(@PathVariable Long itemId, @PathVariable Long variantId) {
        itemVariantService.deleteVariant(variantId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if variant is in stock
     * GET /api/v1/items/{itemId}/variants/{variantId}/in-stock
     */
    @GetMapping("/{variantId}/in-stock")
    public ResponseEntity<Map<String, Boolean>> isVariantInStock(@PathVariable Long itemId, @PathVariable Long variantId) {
        boolean inStock = itemVariantService.isVariantInStock(variantId);
        return ResponseEntity.ok(Map.of("inStock", inStock));
    }

    /**
     * Reserve stock for a variant
     * POST /api/v1/items/{itemId}/variants/{variantId}/reserve
     */
    @PostMapping("/{variantId}/reserve")
    public ResponseEntity<StockOperationResponse> reserveStock(@PathVariable Long itemId,
                                                              @PathVariable Long variantId,
                                                              @Valid @RequestBody StockOperationRequest request) {
        StockOperationResponse response = itemVariantService.reserveStock(variantId, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    /**
     * Release reserved stock for variant
     * POST /api/v1/items/{itemId}/variants/{variantId}/release-reserved
     */
    @PostMapping("/{variantId}/release-reserved")
    public ResponseEntity<StockOperationResponse> releaseReservedStock(@PathVariable Long itemId,
                                                                       @PathVariable Long variantId,
                                                                       @Valid @RequestBody StockOperationRequest request) {
        StockOperationResponse response = itemVariantService.releaseReservedStock(variantId, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    /**
     * Deduct stock for variant (actual sale)
     * POST /api/v1/items/{itemId}/variants/{variantId}/deduct
     */
    @PostMapping("/{variantId}/deduct")
    public ResponseEntity<StockOperationResponse> deductStock(@PathVariable Long itemId,
                                                             @PathVariable Long variantId,
                                                             @Valid @RequestBody StockOperationRequest request) {
        StockOperationResponse response = itemVariantService.deductStock(variantId, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    /**
     * Add stock for variant (restock)
     * POST /api/v1/items/{itemId}/variants/{variantId}/add
     */
    @PostMapping("/{variantId}/add")
    public ResponseEntity<StockOperationResponse> addStock(@PathVariable Long itemId,
                                                          @PathVariable Long variantId,
                                                          @Valid @RequestBody StockOperationRequest request) {
        StockOperationResponse response = itemVariantService.addStock(variantId, request.getQuantity());
        return ResponseEntity.ok(response);
    }
}

