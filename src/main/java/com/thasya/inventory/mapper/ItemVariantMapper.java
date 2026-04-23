package com.thasya.inventory.mapper;

import com.thasya.inventory.dto.ItemVariantDTO;
import com.thasya.inventory.entity.ItemVariant;
import org.springframework.stereotype.Component;

@Component
public class ItemVariantMapper {

    public ItemVariantDTO toDTO(ItemVariant variant) {
        if (variant == null) {
            return null;
        }

        return ItemVariantDTO.builder()
                .id(variant.getId())
                .name(variant.getName())
                .color(variant.getColor())
                .size(variant.getSize())
                .description(variant.getDescription())
                .price(variant.getPrice())
                .itemId(variant.getItem() != null ? variant.getItem().getId() : null)
                .createdAt(variant.getCreatedAt())
                .updatedAt(variant.getUpdatedAt())
                .build();
    }

    public ItemVariant toEntity(ItemVariantDTO dto) {
        if (dto == null) {
            return null;
        }

        return ItemVariant.builder()
                .id(dto.getId())
                .name(dto.getName())
                .color(dto.getColor())
                .size(dto.getSize())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }

    public void updateEntityFromDTO(ItemVariantDTO dto, ItemVariant variant) {
        if (dto == null || variant == null) {
            return;
        }

        if (dto.getName() != null) {
            variant.setName(dto.getName());
        }
        if (dto.getColor() != null) {
            variant.setColor(dto.getColor());
        }
        if (dto.getSize() != null) {
            variant.setSize(dto.getSize());
        }
        if (dto.getDescription() != null) {
            variant.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            variant.setPrice(dto.getPrice());
        }
    }
}

