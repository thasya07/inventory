package com.thasya.inventory.service;

import com.thasya.inventory.dto.ItemVariantDTO;
import com.thasya.inventory.dto.StockOperationResponse;
import com.thasya.inventory.dto.VariantStockDTO;
import com.thasya.inventory.entity.Item;
import com.thasya.inventory.entity.ItemVariant;
import com.thasya.inventory.entity.VariantStock;
import com.thasya.inventory.exception.InsufficientStockException;
import com.thasya.inventory.exception.ResourceNotFoundException;
import com.thasya.inventory.mapper.ItemVariantMapper;
import com.thasya.inventory.mapper.VariantStockMapper;
import com.thasya.inventory.repository.ItemRepository;
import com.thasya.inventory.repository.ItemVariantRepository;
import com.thasya.inventory.repository.VariantStockRepository;
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
public class ItemVariantService {

    private final ItemVariantRepository variantRepository;
    private final ItemRepository itemRepository;
    private final VariantStockRepository variantStockRepository;
    private final ItemVariantMapper variantMapper;
    private final VariantStockMapper variantStockMapper;

    /**
     * Create a new item variant with initial stock
     */
    public ItemVariantDTO createVariant(Long itemId, ItemVariantDTO variantDTO, VariantStockDTO stockDTO) {
        log.info("Creating new variant for item ID: {}", itemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        ItemVariant variant = variantMapper.toEntity(variantDTO);
        variant.setItem(item);
        variant = variantRepository.save(variant);
        log.debug("Variant saved with ID: {}", variant.getId());

        // Create stock for the variant
        if (stockDTO != null) {
            VariantStock stock = variantStockMapper.toEntity(stockDTO);
            stock.setVariant(variant);
            variantStockRepository.save(stock);
            log.debug("Initial stock created for variant: quantity={}, reorderLevel={}",
                     stock.getQuantity(), stock.getReorderLevel());
        }

        log.info("Variant created successfully. ID: {}, Item ID: {}", variant.getId(), itemId);
        return variantMapper.toDTO(variant);
    }

    /**
     * Get all variants for an item
     */
    @Transactional(readOnly = true)
    public List<ItemVariantDTO> getVariantsByItemId(Long itemId) {
        // Check if item exists
        itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        return variantRepository.findByItemId(itemId).stream()
                .map(variantMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get variant by ID
     */
    @Transactional(readOnly = true)
    public ItemVariantDTO getVariantById(Long variantId) {
        ItemVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));
        return variantMapper.toDTO(variant);
    }

    /**
     * Update variant details
     */
    public ItemVariantDTO updateVariant(Long variantId, ItemVariantDTO variantDTO) {
        ItemVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));

        variantMapper.updateEntityFromDTO(variantDTO, variant);
        variant = variantRepository.save(variant);

        return variantMapper.toDTO(variant);
    }

    /**
     * Delete variant
     */
    public void deleteVariant(Long variantId) {
        ItemVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));
        variantRepository.delete(variant);
    }

    /**
     * Check if variant is in stock
     */
    @Transactional(readOnly = true)
    public boolean isVariantInStock(Long variantId) {
        VariantStock stock = variantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for variant: " + variantId));
        return !stock.isOutOfStock();
    }

    /**
     * Reserve stock for a variant
     */
    public StockOperationResponse reserveStock(Long variantId, Integer quantity) {
        log.info("Attempting to reserve {} units of variant stock for variant ID: {}", quantity, variantId);

        if (quantity == null || quantity <= 0) {
            log.warn("Invalid quantity for variant reservation: {}", quantity);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        VariantStock stock = variantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for variant: " + variantId));

        if (stock.getAvailableQuantity() < quantity) {
            log.warn("Insufficient stock for variant reservation. Available: {}, Requested: {}",
                    stock.getAvailableQuantity(), quantity);
            throw new InsufficientStockException(
                    "Insufficient stock for variant. Available: " + stock.getAvailableQuantity() + ", Requested: " + quantity
            );
        }

        int previousReserved = stock.getReserved();
        stock.setReserved(stock.getReserved() + quantity);
        variantStockRepository.save(stock);

        log.info("Variant stock reserved successfully. Variant ID: {}, Quantity: {}, New reserved: {}",
                variantId, quantity, stock.getReserved());

        return StockOperationResponse.builder()
                .message("Stock reserved successfully")
                .itemId(variantId)
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .available(stock.getAvailableQuantity())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Release reserved stock for a variant
     */
    public StockOperationResponse releaseReservedStock(Long variantId, Integer quantity) {
        log.info("Attempting to release {} units of variant reserved stock for variant ID: {}", quantity, variantId);

        if (quantity == null || quantity <= 0) {
            log.warn("Invalid quantity for variant release: {}", quantity);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        VariantStock stock = variantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for variant: " + variantId));

        if (quantity > stock.getReserved()) {
            log.warn("Release quantity {} exceeds reserved {}. Variant ID: {}",
                    quantity, stock.getReserved(), variantId);
            throw new InsufficientStockException(
                    "Cannot release " + quantity + " units. Only " + stock.getReserved() + " units are reserved."
            );
        }

        int previousReserved = stock.getReserved();
        stock.setReserved(stock.getReserved() - quantity);
        variantStockRepository.save(stock);

        log.info("Variant reserved stock released successfully. Variant ID: {}, Quantity: {}, New reserved: {}",
                variantId, quantity, stock.getReserved());

        return StockOperationResponse.builder()
                .message("Reserved stock released successfully")
                .itemId(variantId)
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .available(stock.getAvailableQuantity())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Deduct stock from inventory (actual sale)
     */
    public StockOperationResponse deductStock(Long variantId, Integer quantity) {
        log.info("Attempting to deduct {} units of variant stock for variant ID: {}", quantity, variantId);

        if (quantity == null || quantity <= 0) {
            log.warn("Invalid quantity for variant deduction: {}", quantity);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        VariantStock stock = variantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for variant: " + variantId));

        if (stock.getAvailableQuantity() < quantity) {
            log.warn("Insufficient stock for variant deduction. Available: {}, Requested: {}",
                    stock.getAvailableQuantity(), quantity);
            throw new InsufficientStockException(
                    "Insufficient stock for variant. Available: " + stock.getAvailableQuantity() + ", Requested: " + quantity
            );
        }

        int previousQuantity = stock.getQuantity();
        stock.setQuantity(stock.getQuantity() - quantity);
        if (stock.getReserved() > 0) {
            stock.setReserved(Math.max(0, stock.getReserved() - quantity));
        }
        variantStockRepository.save(stock);

        log.info("Variant stock deducted successfully. Variant ID: {}, Deducted: {}, New quantity: {}",
                variantId, quantity, stock.getQuantity());

        return StockOperationResponse.builder()
                .message("Stock deducted successfully")
                .itemId(variantId)
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .available(stock.getAvailableQuantity())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Add stock to inventory (restock)
     */
    public StockOperationResponse addStock(Long variantId, Integer quantity) {
        log.info("Attempting to add {} units of variant stock for variant ID: {}", quantity, variantId);

        if (quantity == null || quantity <= 0) {
            log.warn("Invalid quantity for adding variant stock: {}", quantity);
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        VariantStock stock = variantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for variant: " + variantId));

        int previousQuantity = stock.getQuantity();
        stock.setQuantity(stock.getQuantity() + quantity);
        variantStockRepository.save(stock);

        log.info("Variant stock added successfully. Variant ID: {}, Added: {}, New quantity: {}",
                variantId, quantity, stock.getQuantity());

        return StockOperationResponse.builder()
                .message("Stock added successfully")
                .itemId(variantId)
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .available(stock.getAvailableQuantity())
                .timestamp(LocalDateTime.now())
                .build();
    }
}

