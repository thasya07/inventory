package com.thasya.inventory.mapper;

import com.thasya.inventory.dto.ItemDTO;
import com.thasya.inventory.entity.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemDTO toDTO(Item item) {
        if (item == null) {
            return null;
        }

        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .sku(item.getSku())
                .description(item.getDescription())
                .basePrice(item.getBasePrice())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    public Item toEntity(ItemDTO dto) {
        if (dto == null) {
            return null;
        }

        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .sku(dto.getSku())
                .description(dto.getDescription())
                .basePrice(dto.getBasePrice())
                .build();
    }

    public void updateEntityFromDTO(ItemDTO dto, Item item) {
        if (dto == null || item == null) {
            return;
        }

        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getBasePrice() != null) {
            item.setBasePrice(dto.getBasePrice());
        }
    }
}

