package com.thasya.inventory.service;

import com.thasya.inventory.dto.VariantStockDTO;
import com.thasya.inventory.entity.VariantStock;
import com.thasya.inventory.exception.ResourceNotFoundException;
import com.thasya.inventory.mapper.VariantStockMapper;
import com.thasya.inventory.repository.VariantStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantStockService {

    private final VariantStockRepository variantStockRepository;
    private final VariantStockMapper variantStockMapper;

    /**
     * Get all variant stocks
     */
    @Transactional(readOnly = true)
    public List<VariantStockDTO> getAllVariantStocks() {
        return variantStockRepository.findAll().stream()
                .map(variantStockMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get variant stock by ID
     */
    @Transactional(readOnly = true)
    public VariantStockDTO getVariantStockById(Long stockId) {
        VariantStock stock = variantStockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant stock not found with id: " + stockId));
        return variantStockMapper.toDTO(stock);
    }

    /**
     * Get stock for a variant
     */
    @Transactional(readOnly = true)
    public VariantStockDTO getStockByVariantId(Long variantId) {
        VariantStock stock = variantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for variant: " + variantId));
        return variantStockMapper.toDTO(stock);
    }

    /**
     * Update variant stock details
     */
    public VariantStockDTO updateVariantStock(Long stockId, VariantStockDTO stockDTO) {
        VariantStock stock = variantStockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant stock not found with id: " + stockId));

        variantStockMapper.updateEntityFromDTO(stockDTO, stock);
        stock = variantStockRepository.save(stock);

        return variantStockMapper.toDTO(stock);
    }

    /**
     * Get all low stock variants
     */
    @Transactional(readOnly = true)
    public List<VariantStockDTO> getLowStockVariants() {
        return variantStockRepository.findAll().stream()
                .filter(VariantStock::isLowStock)
                .map(variantStockMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all out of stock variants
     */
    @Transactional(readOnly = true)
    public List<VariantStockDTO> getOutOfStockVariants() {
        return variantStockRepository.findAll().stream()
                .filter(VariantStock::isOutOfStock)
                .map(variantStockMapper::toDTO)
                .collect(Collectors.toList());
    }
}

