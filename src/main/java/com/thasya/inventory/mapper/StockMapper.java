package com.thasya.inventory.mapper;

import com.thasya.inventory.dto.StockDTO;
import com.thasya.inventory.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {

    public StockDTO toDTO(Stock stock) {
        if (stock == null) {
            return null;
        }

        return StockDTO.builder()
                .id(stock.getId())
                .quantity(stock.getQuantity())
                .reserved(stock.getReserved())
                .reorderLevel(stock.getReorderLevel())
                .availableQuantity(stock.getAvailableQuantity())
                .outOfStock(stock.isOutOfStock())
                .lowStock(stock.isLowStock())
                .itemId(stock.getItem() != null ? stock.getItem().getId() : null)
                .build();
    }

    public Stock toEntity(StockDTO dto) {
        if (dto == null) {
            return null;
        }

        return Stock.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .reserved(dto.getReserved() != null ? dto.getReserved() : 0)
                .reorderLevel(dto.getReorderLevel() != null ? dto.getReorderLevel() : 10)
                .build();
    }

    public void updateEntityFromDTO(StockDTO dto, Stock stock) {
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

