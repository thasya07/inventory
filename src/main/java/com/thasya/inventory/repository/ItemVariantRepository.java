package com.thasya.inventory.repository;

import com.thasya.inventory.entity.ItemVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemVariantRepository extends JpaRepository<ItemVariant, Long> {
    List<ItemVariant> findByItemId(Long itemId);
}

