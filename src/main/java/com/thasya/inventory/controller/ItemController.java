package com.thasya.inventory.controller;

import com.thasya.inventory.dto.*;
import com.thasya.inventory.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API Controller for Item Management
 * Base path: /api/v1/items
 */
@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * Create a new item
     * POST /api/v1/items
     */
    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemCreationRequest request) {
        ItemDTO created = itemService.createItem(request.getItem(), request.getStock());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get all items
     * GET /api/v1/items
     */
    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        List<ItemDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Get item by ID
     * GET /api/v1/items/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        ItemDTO item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    /**
     * Get item by SKU
     * GET /api/v1/items/sku/{sku}
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ItemDTO> getItemBySku(@PathVariable String sku) {
        ItemDTO item = itemService.getItemBySku(sku);
        return ResponseEntity.ok(item);
    }

    /**
     * Update item
     * PUT /api/v1/items/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTO itemDTO) {
        ItemDTO updated = itemService.updateItem(id, itemDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete item
     * DELETE /api/v1/items/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if item is in stock
     * GET /api/v1/items/{id}/in-stock
     */
    @GetMapping("/{id}/in-stock")
    public ResponseEntity<Map<String, Boolean>> isItemInStock(@PathVariable Long id) {
        boolean inStock = itemService.isItemInStock(id);
        return ResponseEntity.ok(Map.of("inStock", inStock));
    }

    /**
     * Reserve stock for an item
     * POST /api/v1/items/{id}/reserve
     */
    @PostMapping("/{id}/reserve")
    public ResponseEntity<StockOperationResponse> reserveStock(@PathVariable Long id,
                                                               @Valid @RequestBody StockOperationRequest request) {
        StockOperationResponse response = itemService.reserveStock(id, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    /**
     * Release reserved stock
     * POST /api/v1/items/{id}/release-reserved
     */
    @PostMapping("/{id}/release-reserved")
    public ResponseEntity<StockOperationResponse> releaseReservedStock(@PathVariable Long id,
                                                                       @Valid @RequestBody StockOperationRequest request) {
        StockOperationResponse response = itemService.releaseReservedStock(id, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    /**
     * Deduct stock (actual sale)
     * POST /api/v1/items/{id}/deduct
     */
    @PostMapping("/{id}/deduct")
    public ResponseEntity<StockOperationResponse> deductStock(@PathVariable Long id,
                                                              @Valid @RequestBody StockOperationRequest request) {
        StockOperationResponse response = itemService.deductStock(id, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    /**
     * Add stock (restock)
     * POST /api/v1/items/{id}/add
     */
    @PostMapping("/{id}/add")
    public ResponseEntity<StockOperationResponse> addStock(@PathVariable Long id,
                                                           @Valid @RequestBody StockOperationRequest request) {
        StockOperationResponse response = itemService.addStock(id, request.getQuantity());
        return ResponseEntity.ok(response);
    }
}

