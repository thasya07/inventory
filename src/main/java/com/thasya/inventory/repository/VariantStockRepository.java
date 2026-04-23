package com.thasya.inventory.repository;

import com.thasya.inventory.entity.VariantStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VariantStockRepository extends JpaRepository<VariantStock, Long> {
    Optional<VariantStock> findByVariantId(Long variantId);
}

