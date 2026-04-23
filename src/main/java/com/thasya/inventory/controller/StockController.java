package com.thasya.inventory.controller;

import com.thasya.inventory.dto.StockDTO;
import com.thasya.inventory.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API Controller for Stock Management
 * Base path: /api/v1/stocks
 */
@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /**
     * Get all stocks
     * GET /api/v1/stocks
     */
    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        List<StockDTO> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    /**
     * Get stock by ID
     * GET /api/v1/stocks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockById(@PathVariable Long id) {
        StockDTO stock = stockService.getStockById(id);
        return ResponseEntity.ok(stock);
    }

    /**
     * Get stock for an item
     * GET /api/v1/stocks/item/{itemId}
     */
    @GetMapping("/item/{itemId}")
    public ResponseEntity<StockDTO> getStockByItemId(@PathVariable Long itemId) {
        StockDTO stock = stockService.getStockByItemId(itemId);
        return ResponseEntity.ok(stock);
    }

    /**
     * Update stock
     * PUT /api/v1/stocks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Long id, @Valid @RequestBody StockDTO stockDTO) {
        StockDTO updated = stockService.updateStock(id, stockDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Get all low stock items
     * GET /api/v1/stocks/alerts/low-stock
     */
    @GetMapping("/alerts/low-stock")
    public ResponseEntity<List<StockDTO>> getLowStockItems() {
        List<StockDTO> lowStockItems = stockService.getLowStockItems();
        return ResponseEntity.ok(lowStockItems);
    }

    /**
     * Get all out of stock items
     * GET /api/v1/stocks/alerts/out-of-stock
     */
    @GetMapping("/alerts/out-of-stock")
    public ResponseEntity<List<StockDTO>> getOutOfStockItems() {
        List<StockDTO> outOfStockItems = stockService.getOutOfStockItems();
        return ResponseEntity.ok(outOfStockItems);
    }
}

