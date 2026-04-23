package com.thasya.inventory.service;

import com.thasya.inventory.dto.StockDTO;
import com.thasya.inventory.entity.Item;
import com.thasya.inventory.entity.Stock;
import com.thasya.inventory.exception.ResourceNotFoundException;
import com.thasya.inventory.mapper.StockMapper;
import com.thasya.inventory.repository.ItemRepository;
import com.thasya.inventory.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final StockRepository stockRepository;
    private final ItemRepository itemRepository;
    private final StockMapper stockMapper;

    /**
     * Get all stocks
     */
    @Transactional(readOnly = true)
    public List<StockDTO> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(stockMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get stock by ID
     */
    @Transactional(readOnly = true)
    public StockDTO getStockById(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + stockId));
        return stockMapper.toDTO(stock);
    }

    /**
     * Get stock for an item
     */
    @Transactional(readOnly = true)
    public StockDTO getStockByItemId(Long itemId) {
        Stock stock = stockRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for item: " + itemId));
        return stockMapper.toDTO(stock);
    }

    /**
     * Update stock details
     */
    public StockDTO updateStock(Long stockId, StockDTO stockDTO) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + stockId));

        stockMapper.updateEntityFromDTO(stockDTO, stock);
        stock = stockRepository.save(stock);

        return stockMapper.toDTO(stock);
    }

    /**
     * Get all low stock items
     */
    @Transactional(readOnly = true)
    public List<StockDTO> getLowStockItems() {
        return stockRepository.findAll().stream()
                .filter(Stock::isLowStock)
                .map(stockMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all out of stock items
     */
    @Transactional(readOnly = true)
    public List<StockDTO> getOutOfStockItems() {
        return stockRepository.findAll().stream()
                .filter(Stock::isOutOfStock)
                .map(stockMapper::toDTO)
                .collect(Collectors.toList());
    }
}

