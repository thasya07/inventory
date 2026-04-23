package com.thasya.inventory.service;

import com.thasya.inventory.dto.ItemDTO;
import com.thasya.inventory.dto.StockDTO;
import com.thasya.inventory.dto.StockOperationResponse;
import com.thasya.inventory.entity.Item;
import com.thasya.inventory.entity.Stock;
import com.thasya.inventory.exception.DuplicateResourceException;
import com.thasya.inventory.exception.InsufficientStockException;
import com.thasya.inventory.exception.ResourceNotFoundException;
import com.thasya.inventory.mapper.ItemMapper;
import com.thasya.inventory.mapper.StockMapper;
import com.thasya.inventory.repository.ItemRepository;
import com.thasya.inventory.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final StockRepository stockRepository;
    private final ItemMapper itemMapper;
    private final StockMapper stockMapper;

    /**
     * Create a new item with initial stock
     */
    public ItemDTO createItem(ItemDTO itemDTO, StockDTO stockDTO) {
        log.info("Creating new item with SKU: {}", itemDTO.getSku());
        
        if (itemRepository.existsBySku(itemDTO.getSku())) {
            log.warn("Duplicate item creation attempted with SKU: {}", itemDTO.getSku());
            throw new DuplicateResourceException("Item with SKU '" + itemDTO.getSku() + "' already exists");
        }

        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        log.debug("Item saved with ID: {}", item.getId());

        // Create stock for the item
        if (stockDTO != null) {
            Stock stock = stockMapper.toEntity(stockDTO);
            stock.setItem(item);
            stockRepository.save(stock);
            log.debug("Initial stock created: quantity={}, reorderLevel={}", 
                     stock.getQuantity(), stock.getReorderLevel());
        }

        log.info("Item created successfully. ID: {}, SKU: {}", item.getId(), item.getSku());
        return itemMapper.toDTO(item);
    }

    /**
     * Get all items
     */
    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get item by ID
     */
    @Transactional(readOnly = true)
    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        return itemMapper.toDTO(item);
    }

    /**
     * Get item by SKU
     */
    @Transactional(readOnly = true)
    public ItemDTO getItemBySku(String sku) {
        Item item = itemRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with SKU: " + sku));
        return itemMapper.toDTO(item);
    }

    /**
     * Update item details
     */
    public ItemDTO updateItem(Long id, ItemDTO itemDTO) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

        // Check if new SKU is already taken by another item
        if (itemDTO.getSku() != null && !itemDTO.getSku().equals(item.getSku())) {
            if (itemRepository.existsBySku(itemDTO.getSku())) {
                throw new DuplicateResourceException("Item with SKU '" + itemDTO.getSku() + "' already exists");
            }
            item.setSku(itemDTO.getSku());
        }

        itemMapper.updateEntityFromDTO(itemDTO, item);
        item = itemRepository.save(item);

        return itemMapper.toDTO(item);
    }

    /**
     * Delete item
     */
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        itemRepository.delete(item);
    }

    /**
     * Check if item is in stock
     */
    @Transactional(readOnly = true)
    public boolean isItemInStock(Long itemId) {
        Stock stock = stockRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for item: " + itemId));
        return !stock.isOutOfStock();
    }

    /**
     * Reserve stock for an item (e.g., for an order)
     */
    public StockOperationResponse reserveStock(Long itemId, Integer quantity) {
        log.info("Attempting to reserve {} units of stock for item ID: {}", quantity, itemId);
        
        if (quantity == null || quantity <= 0) {
            log.warn("Invalid quantity for reservation: {}", quantity);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        Stock stock = stockRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for item: " + itemId));

        if (stock.getAvailableQuantity() < quantity) {
            log.warn("Insufficient stock for reservation. Available: {}, Requested: {}", 
                    stock.getAvailableQuantity(), quantity);
            throw new InsufficientStockException(
                    "Insufficient stock. Available: " + stock.getAvailableQuantity() + ", Requested: " + quantity
            );
        }

        int previousReserved = stock.getReserved();
        stock.setReserved(stock.getReserved() + quantity);
        stockRepository.save(stock);
        
        log.info("Stock reserved successfully. Item ID: {}, Quantity: {}, Previous reserved: {}, New reserved: {}", 
                itemId, quantity, previousReserved, stock.getReserved());
        
        return StockOperationResponse.builder()
                .message("Stock reserved successfully")
                .itemId(itemId)
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .available(stock.getAvailableQuantity())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Release reserved stock
     */
    public StockOperationResponse releaseReservedStock(Long itemId, Integer quantity) {
        log.info("Attempting to release {} units of reserved stock for item ID: {}", quantity, itemId);

        if (quantity == null || quantity <= 0) {
            log.warn("Invalid quantity for release: {}", quantity);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Stock stock = stockRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for item: " + itemId));

        if (quantity > stock.getReserved()) {
            log.warn("Release quantity {} exceeds reserved {}. Item ID: {}",
                    quantity, stock.getReserved(), itemId);
            throw new InsufficientStockException(
                    "Cannot release " + quantity + " units. Only " + stock.getReserved() + " units are reserved."
            );
        }

        int previousReserved = stock.getReserved();
        stock.setReserved(stock.getReserved() - quantity);
        stockRepository.save(stock);

        log.info("Reserved stock released successfully. Item ID: {}, Quantity: {}, Previous reserved: {}, New reserved: {}",
                itemId, quantity, previousReserved, stock.getReserved());

        return StockOperationResponse.builder()
                .message("Reserved stock released successfully")
                .itemId(itemId)
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .available(stock.getAvailableQuantity())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Deduct stock from inventory (actual sale)
     */
    public StockOperationResponse deductStock(Long itemId, Integer quantity) {
        log.info("Attempting to deduct {} units of stock for item ID: {}", quantity, itemId);

        if (quantity == null || quantity <= 0) {
            log.warn("Invalid quantity for deduction: {}", quantity);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Stock stock = stockRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for item: " + itemId));

        if (stock.getAvailableQuantity() < quantity) {
            log.warn("Insufficient stock for deduction. Available: {}, Requested: {}",
                    stock.getAvailableQuantity(), quantity);
            throw new InsufficientStockException(
                    "Insufficient stock. Available: " + stock.getAvailableQuantity() + ", Requested: " + quantity
            );
        }

        int previousQuantity = stock.getQuantity();
        int previousReserved = stock.getReserved();

        stock.setQuantity(stock.getQuantity() - quantity);
        if (stock.getReserved() > 0) {
            stock.setReserved(Math.max(0, stock.getReserved() - quantity));
        }
        stockRepository.save(stock);

        log.info("Stock deducted successfully. Item ID: {}, Deducted: {}, Previous quantity: {}, New quantity: {}",
                itemId, quantity, previousQuantity, stock.getQuantity());

        return StockOperationResponse.builder()
                .message("Stock deducted successfully")
                .itemId(itemId)
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .available(stock.getAvailableQuantity())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Add stock to inventory (restock)
     */
    public StockOperationResponse addStock(Long itemId, Integer quantity) {
        log.info("Attempting to add {} units of stock for item ID: {}", quantity, itemId);

        if (quantity == null || quantity <= 0) {
            log.warn("Invalid quantity for adding stock: {}", quantity);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Stock stock = stockRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for item: " + itemId));

        int previousQuantity = stock.getQuantity();
        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.save(stock);

        log.info("Stock added successfully. Item ID: {}, Added: {}, Previous quantity: {}, New quantity: {}",
                itemId, quantity, previousQuantity, stock.getQuantity());

        return StockOperationResponse.builder()
                .message("Stock added successfully")
                .itemId(itemId)
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .available(stock.getAvailableQuantity())
                .timestamp(LocalDateTime.now())
                .build();
    }
}

