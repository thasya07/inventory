package com.thasya.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Reserved quantity cannot be null")
    @Min(value = 0, message = "Reserved quantity cannot be negative")
    @Column(nullable = false)
    @Builder.Default
    private Integer reserved = 0;

    @NotNull(message = "Reorder level cannot be null")
    @Min(value = 0, message = "Reorder level cannot be negative")
    @Column(nullable = false)
    @Builder.Default
    private Integer reorderLevel = 10;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, unique = true)
    private Item item;

    public Integer getAvailableQuantity() {
        return quantity - reserved;
    }

    public boolean isOutOfStock() {
        return getAvailableQuantity() <= 0;
    }

    public boolean isLowStock() {
        return getAvailableQuantity() <= reorderLevel;
    }
}

