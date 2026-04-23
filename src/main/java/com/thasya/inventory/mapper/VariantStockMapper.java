package com.thasya.inventory.mapper;

import com.thasya.inventory.dto.VariantStockDTO;
import com.thasya.inventory.entity.VariantStock;
import org.springframework.stereotype.Component;

@Component
public class VariantStockMapper {

    public VariantStockDTO toDTO(VariantStock stock) {
        if (stock == null) {
            return null;
        }

        return VariantStockDTO.builder()
                .id(stock.getId())
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .reorderLevel(stock.getReorderLevel())
                .availableQuantity(stock.getAvailableQuantity())
                .outOfStock(stock.isOutOfStock())
                .lowStock(stock.isLowStock())
                .variantId(stock.getVariant() != null ? stock.getVariant().getId() : null)
                .build();
    }

    public VariantStock toEntity(VariantStockDTO dto) {
        if (dto == null) {
            return null;
        }

        return VariantStock.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .reserved(dto.getReserved() != null ? dto.getReserved() : 0)
                .reorderLevel(dto.getReorderLevel() != null ? dto.getReorderLevel() : 5)
                .build();
    }

    public void updateEntityFromDTO(VariantStockDTO dto, VariantStock stock) {
        if (dto == null || stock == null) {
            return;
        }

        if (dto.getQuantity() != null) {
            stock.setQuantity(dto.getQuantity());
        }
        if (dto.getReserved() != null) {
            stock.setReserved(dto.getReserved());
        }
        if (dto.getReorderLevel() != null) {
            stock.setReorderLevel(dto.getReorderLevel());
        }
    }
}

